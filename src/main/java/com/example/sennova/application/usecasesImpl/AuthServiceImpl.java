package com.example.sennova.application.usecasesImpl;

import com.example.sennova.application.dto.UserDtos.UserPreferenceResponse;
import com.example.sennova.application.dto.UserDtos.UserResponse;
import com.example.sennova.application.dto.authDto.*;
import com.example.sennova.application.usecases.AuthUseCase;
import com.example.sennova.application.usecases.UserUseCase;
import com.example.sennova.domain.model.UserModel;
import com.example.sennova.infrastructure.persistence.entities.PasswordResetToken;
import com.example.sennova.infrastructure.persistence.entities.UserEntity;
import com.example.sennova.infrastructure.persistence.entities.VerificationEmail;
import com.example.sennova.infrastructure.persistence.repositoryJpa.PasswordResetTokenJpaRepository;
import com.example.sennova.infrastructure.persistence.repositoryJpa.VerificationEmailRepositoryJpa;
import com.example.sennova.infrastructure.restTemplate.AuthEmailService;
import com.example.sennova.web.exception.AuthenticationException;
import com.example.sennova.web.exception.EntityNotFoundException;
import com.example.sennova.web.security.GoogleAuthService;
import com.example.sennova.web.security.JwtUtils;
import com.example.sennova.web.security.UserServiceSecurity;
import com.example.sennova.web.security.UserSystemUserDetails;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AuthServiceImpl {
    private final UserUseCase userUseCase;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserServiceSecurity userServiceSecurity;
    private final GoogleAuthService googleAuthService;
    private final VerificationEmailRepositoryJpa verificationEmailRepositoryJpa;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenJpaRepository passwordResetTokenJpaRepository;
    private final AuthEmailService emailService;

    @Autowired
    public AuthServiceImpl(UserUseCase userUseCase, AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserServiceSecurity userServiceSecurity, GoogleAuthService googleAuthService, VerificationEmailRepositoryJpa verificationEmailRepositoryJpa, PasswordEncoder passwordEncoder, PasswordResetTokenJpaRepository passwordResetTokenJpaRepository, AuthEmailService emailService) {
        this.userUseCase = userUseCase;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userServiceSecurity = userServiceSecurity;
        this.googleAuthService = googleAuthService;
        this.verificationEmailRepositoryJpa = verificationEmailRepositoryJpa;
        this.passwordEncoder = passwordEncoder;
        this.passwordResetTokenJpaRepository = passwordResetTokenJpaRepository;
        this.emailService = emailService;
    }

    @Transactional
    public Map<String, Object> login(LoginRequestDto loginRequestDto) {

        try {
            Authentication authentication = this.authentication(loginRequestDto.username(), loginRequestDto.password());
            UserSystemUserDetails user = (UserSystemUserDetails) authentication.getPrincipal();

            UserModel userModel = this.userUseCase.findByUsername(user.getUsername());

            if(!userModel.getAvailable()){
                throw new IllegalArgumentException("Tu cuenta esta desactivada.");
            }

            String authority = user.getAuthorities().iterator().next().getAuthority();
            HashMap<String, String> jwt = (HashMap<String, String>) this.jwtUtils.createJwt(user.getUsername(), authority);


            UserPreferenceResponse userPreferenceResponse = new UserPreferenceResponse(userModel.isNotifyEquipment(), userModel.isNotifyReagents(), userModel.isNotifyQuotes(), userModel.isNotifyResults());
            LoginResponseDto response = new LoginResponseDto(jwt.get("access-token"), userModel.getUserId(), true, "Logged success", userModel.getPosition(), userModel.getImageProfile(), LocalDate.now(), authority, true, userModel.getAvailable() , userModel.getUsername(), userModel.getName(), userPreferenceResponse, userModel.getEmail(), LocalDateTime.now());

            this.userUseCase.saveRefreshToken(jwt.get("refresh-token"), user.getUsername());
            this.userUseCase.saveTheLastSession(LocalDateTime.now(), userModel.getUserId());

            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put("refreshToken", jwt.get("refresh-token"));
            objectMap.put("response", response);
            return objectMap;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    @Transactional
    public Map<String, Object> signInWithGoogle(Map<String, String> body) {
        String idToken = body.get("token");

        var payload = googleAuthService.verifyToken(idToken);

        String email = payload.getEmail();
        String name = (String) payload.get("name");

        UserModel userModel = this.userUseCase.getByEmail(email);
        String authority = "ROLE_" + userModel.getRole().getNameRole();

        HashMap<String, String> jwt = (HashMap<String, String>) this.jwtUtils.createJwt(userModel.getUsername(), authority);
        UserPreferenceResponse userPreferenceResponse = new UserPreferenceResponse(userModel.isNotifyEquipment(), userModel.isNotifyReagents(), userModel.isNotifyQuotes(), userModel.isNotifyResults());
        LoginResponseDto response = new LoginResponseDto(jwt.get("access-token"), userModel.getUserId(), true, "Logged success", userModel.getPosition(), userModel.getImageProfile(), LocalDate.now(), authority, true, userModel.getAvailable() ,userModel.getUsername(), userModel.getName(), userPreferenceResponse, userModel.getEmail(), LocalDateTime.now());

        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("response", response);

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", jwt.get("refresh-token"))
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofDays(7))
                .sameSite("None")
                .build();

        objectMap.put("cookie", refreshCookie);

        this.userUseCase.saveRefreshToken(jwt.get("refresh-token"), userModel.getUsername());
        this.userUseCase.saveTheLastSession(LocalDateTime.now(), userModel.getUserId());
        return objectMap;
    }

    public void logout(String username) {
        this.userUseCase.deleteRefreshToken(username);
    }

    // method for validate with spring security if the username and password are correct
    public Authentication authentication(@Valid String username, @Valid String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                username,
                password
        );
        return this.authenticationManager.authenticate(authenticationToken);
    }

    @Transactional
    public Map<String, Object> refreshToken(String refreshToken) {

        System.out.println("🔎 Refresh recibido: " + refreshToken);

        if (!this.jwtUtils.validateJwt(refreshToken)) {
            System.out.println("❌ Token inválido");
            throw new RuntimeException("Token inválido");
        }

        String username = this.jwtUtils.getUsername(refreshToken);
        System.out.println("👤 Username del token: " + username);

        UserModel userModel = this.userUseCase.findByUsername(username);

        System.out.println("💾 Refresh en DB: " + userModel.getRefreshToken());

        if (userModel.getRefreshToken() == null) {
            System.out.println("🚨 EL REFRESH EN DB ES NULL");
            throw new RuntimeException("Refresh en DB es null");
        }

        if (!userModel.getRefreshToken().equals(refreshToken)) {
            System.out.println("🚨 EL TOKEN NO COINCIDE CON EL DE DB");
            throw new RuntimeException("Refresh no coincide");
        }

        UserDetails user = this.userServiceSecurity.loadUserByUsername(username);

        String jwt = jwtUtils.generateSingleAccessToken(
                user.getUsername(),
                user.getAuthorities().iterator().next().getAuthority()
        );

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", userModel.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofDays(7))
                .sameSite("None")
                .build();

        Map<String, Object> response = new HashMap<>();
        response.put("response", new LoginResponseDto(
                jwt,
                userModel.getUserId(),
                true,
                "Logged success",
                userModel.getPosition(),
                userModel.getImageProfile(),
                LocalDate.now(),
                user.getAuthorities().iterator().next().getAuthority(),
                true,
                userModel.getAvailable(),
                userModel.getUsername(),
                userModel.getName(),
                new UserPreferenceResponse(
                        userModel.isNotifyEquipment(),
                        userModel.isNotifyReagents(),
                        userModel.isNotifyQuotes(),
                        userModel.isNotifyResults()
                ),
                userModel.getEmail(),
                LocalDateTime.now()
        ));

        response.put("refreshToken", refreshCookie);

        return response;
    }



    @Transactional
    public void generateTokenChangeEmail(@Valid String currentEmail, @Valid String newEmail) {

        if (this.userUseCase.existByEmail(newEmail)) {
            throw new IllegalArgumentException("El correo electronico no esta disponible. ");
        }

        UserModel userModel = this.userUseCase.getByEmail(currentEmail);

        if (!userModel.getAvailable()) {
            throw new IllegalArgumentException("No se puede cambiar el email del usuario porque su cuenta está desactivada.");
        }

        UserEntity userEntity = this.userUseCase.getEntity(userModel.getUserId());


        Optional<VerificationEmail> existingVerification = this.verificationEmailRepositoryJpa.findByUser(userEntity);
        if (existingVerification.isPresent()) {
            VerificationEmail verificationEmail = existingVerification.get();

            if (verificationEmail.getExpiryDate().isAfter(LocalDateTime.now())) {
                throw new IllegalArgumentException("El usuario ya tiene un código válido. Intente nuevamente más tarde.");
            } else {
                this.verificationEmailRepositoryJpa.deleteByUser(userEntity.getUserId());
            }
            
        }

        Integer token = null;
        int attempts = 0;
        Random random = new Random();

        while (attempts < 10) {
            int tokenGenerated = 1000 + random.nextInt(9000);
            if (!this.verificationEmailRepositoryJpa.existsByCode(tokenGenerated)) {
                token = tokenGenerated;
                break;
            }
            attempts++;
        }

        if (token == null) {
            throw new RuntimeException("No fue posible generar un token único.");
        }

        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(5);

        VerificationEmail newVerification = new VerificationEmail(token, expiryDate, newEmail, userEntity);
        this.verificationEmailRepositoryJpa.save(newVerification);


        // send the email
        this.emailService.sendVerificationCode(newEmail, token);


    }

    @Transactional
    public String validateCodeChangeEmail(@Valid Integer code) {

        if (!this.verificationEmailRepositoryJpa.existsByCode(code)) {
            throw new IllegalArgumentException("El codigo que intentar usar no existe, por favor genera uno nuevo e intentalo nuevamente.");
        }

        VerificationEmail verificationEmail = this.verificationEmailRepositoryJpa.findByCode(code);

        if (verificationEmail.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("El codigo que intentas usar ya expiro, genera uno nuevamente.");
        }

        if (!verificationEmail.getCode().equals(code)) {
            throw new IllegalArgumentException("El codigo es incorrecto.");
        }

        String newEmailUpdated = this.userUseCase.changeEmail(verificationEmail.getUser().getEmail(), verificationEmail.getNewEmail());


        return newEmailUpdated;
    }

    public boolean changePassword(@Valid String username, ChangePasswordRequest request) {
        UserModel userModel = this.userUseCase.findByUsername(username);

        if (passwordEncoder.matches(request.newPassword(), userModel.getPassword())) {
            throw new IllegalArgumentException("La nueva contraseña no puede ser igual a la anterior.");
        }

        if (!passwordEncoder.matches(request.password(), userModel.getPassword())) {
            throw new IllegalArgumentException("La contraseña actual es incorrecta.");
        }

        String passwordEncoded = passwordEncoder.encode(request.newPassword());
        userModel.setPassword(passwordEncoded);
        UserResponse userResponse = this.userUseCase.saveModel(userModel);

       return true;
    }

    @Transactional
    public void resetPassword(ChangePasswordLoginDto changePasswordLoginDto){

        // validate the token  and get it
        PasswordResetToken passwordResetToken = this.validateAndGetToken(changePasswordLoginDto.getToken());


        // 2. get the user
        UserModel user = this.userUseCase.getByEmail(passwordResetToken.getEmail());


        // 3, change the password and update
        String newPasswordEncode = passwordEncoder.encode(changePasswordLoginDto.getNewPassword());

        user.setPassword(newPasswordEncode);
        this.userUseCase.saveModel(user);

        // delete the token
        this.passwordResetTokenJpaRepository.deleteById(passwordResetToken.getId());



    }

    public void generateResetToken(@Email String email) {

        UserModel user = null;

        try {
            user = this.userUseCase.getByEmail(email);
        } catch (Exception e) {

        }

        if (user != null) {


            String token = UUID.randomUUID().toString();

            String hashedToken = DigestUtils.sha256Hex(token);

            PasswordResetToken passwordResetToken = new PasswordResetToken();
            passwordResetToken.setToken(hashedToken);
            passwordResetToken.setExpireDate(LocalDateTime.now().plusMinutes(2));
            passwordResetToken.setUserId(user.getUserId());
            passwordResetToken.setEmail(email);

            this.passwordResetTokenJpaRepository.save(passwordResetToken);

            this.emailService.sendAccessLink(email, token);
        }

    }


    public PasswordResetToken validateAndGetToken(String token){
         String tokenHashed = DigestUtils.sha256Hex(token);

        Optional<PasswordResetToken> passwordResetToken = this.passwordResetTokenJpaRepository.findByToken(tokenHashed);
         if(passwordResetToken.isEmpty()) throw new AuthenticationException("");

         if(passwordResetToken.get().isExpired()) {

             this.passwordResetTokenJpaRepository.deleteById(passwordResetToken.get().getId());
             throw new AuthenticationException("");
         }


        return passwordResetToken.get();
    }


}
