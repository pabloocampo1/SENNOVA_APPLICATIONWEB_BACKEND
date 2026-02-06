package com.example.sennova.web.controllers;

import com.example.sennova.application.dto.UserDtos.*;
import com.example.sennova.application.usecases.UserUseCase;
import com.example.sennova.domain.model.UserModel;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserUseCase userUseCase;

    @Autowired
    public UserController(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    @GetMapping(path = "/getAll")
    public ResponseEntity<List<UserListResponse>> getAll() {
        return new ResponseEntity<>(this.userUseCase.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/getAllAvailable")
    public ResponseEntity<List<UserResponse>> getAllAvailable() {
        return new ResponseEntity<>(this.userUseCase.getAllAvailable(), HttpStatus.OK);
    }

    @GetMapping(path = "/getById/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable("id") @Valid Long id) {
        return new ResponseEntity<>(this.userUseCase.findById(id), HttpStatus.OK);
    }

    @GetMapping(path = "/getByEmail/{email}")
    public ResponseEntity<UserResponse> getByEmail(@PathVariable("email") @Valid String email) {
        return new ResponseEntity<>(this.userUseCase.getUserResponseByEmail(email), HttpStatus.OK);
    }
    
    @GetMapping(path = "/getByName/{name}")
    public ResponseEntity<List<UserResponse>> findByName(@PathVariable("name") @Valid String name) {
        return new ResponseEntity<>(this.userUseCase.findByName(name), HttpStatus.OK);
    }

    @GetMapping(path = "/getByDni/{dni}")
    public ResponseEntity<List<UserResponse>> findByDni(@PathVariable("dni") @Valid Long dni) {
        return new ResponseEntity<>(this.userUseCase.findByDni(dni), HttpStatus.OK);
    }

    @GetMapping(path = "/getByRole/{roleId}")
    public ResponseEntity<List<UserResponse>> findByRole(@PathVariable("roleId") @Valid Long roleId) {
        return new ResponseEntity<>(this.userUseCase.findByRole(roleId), HttpStatus.OK);
    }

    @GetMapping("/available-with-competencies")
    public ResponseEntity<List<UserCompetenceDTO>> getAvailableUsers() {
        System.out.println("ALMENOS ENTRO");
        List<UserCompetenceDTO> users = userUseCase.getUsersWithCompetencies();
        return ResponseEntity.ok(users);
    }


    @PostMapping(path = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponse> saveUser(@RequestPart("dto") @Valid UserSaveRequest userSaveRequest, @RequestPart(value = "image", required = false) MultipartFile imageFile) {
        return new ResponseEntity<>(this.userUseCase.save(userSaveRequest, imageFile), HttpStatus.CREATED);

    }

    @PutMapping(path = "/update/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponse> update(
            @RequestPart("dto") @Valid UserUpdateDto userUpdateDto,
            @RequestPart(value = "image", required = false) MultipartFile imageFile,
            @PathVariable("userId") Long userId) {

        return new ResponseEntity<>(this.userUseCase.update(userId, userUpdateDto, imageFile), HttpStatus.OK);
    }

    @PutMapping(value = "/updateProfile", consumes = {"multipart/form-data"})
    public ResponseEntity<UserResponse> updatePersonalInfo(
            @RequestParam("userId") Long userId,
            @RequestParam("name") String name,
            @RequestParam("jobPosition") String jobPosition,
            @RequestParam("phoneNumber") Long phoneNumber,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        // 1. Create the dto
        UserUpdateProfileRequest userUpdateProfileRequest = new UserUpdateProfileRequest();
        userUpdateProfileRequest.setUserId(userId);
        userUpdateProfileRequest.setName(name);
        userUpdateProfileRequest.setJobPosition(jobPosition);
        userUpdateProfileRequest.setPhoneNumber(phoneNumber);
        userUpdateProfileRequest.setImage(image);

       UserResponse updatedUser = this.userUseCase.updateUserProfile(userUpdateProfileRequest);


        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") Long userId) {
        try {
            this.userUseCase.deleteUser(userId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/changePreferences/{username}")
    public ResponseEntity<UserPreferenceResponse> changePreferences(@RequestBody UserPreferencesRequestDto userPreferencesRequestDto, @PathVariable("username") String username) {
        return new ResponseEntity<>(this.userUseCase.changePreference(userPreferencesRequestDto, username), HttpStatus.OK);
    }

    @PostMapping("/deactiveAccount/{username}")
    public ResponseEntity<Boolean> deactiveAccount(@PathVariable("username") String username) {
        return new ResponseEntity<>(this.userUseCase.deactiveAccount(username), HttpStatus.OK);
    }

    @PostMapping("/activeAccount/{username}")
    public ResponseEntity<Boolean> activeAccount(@PathVariable("username") String username) {
        return new ResponseEntity<>(this.userUseCase.activeAccount(username), HttpStatus.OK);
    }


}
