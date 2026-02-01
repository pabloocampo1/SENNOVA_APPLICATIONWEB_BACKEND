package com.example.sennova.web.security;

import com.example.sennova.application.usecases.UserUseCase;
import com.example.sennova.domain.model.UserModel;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserServiceSecurity userServiceSecurity;
    private final UserUseCase userUseCase;

    @Autowired
    public JwtFilter(JwtUtils jwtUtils, UserServiceSecurity userServiceSecurity, @Lazy UserUseCase userUseCase) {
        this.jwtUtils = jwtUtils;
        this.userServiceSecurity = userServiceSecurity;
        this.userUseCase = userUseCase;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();
        if (path.startsWith("/api/v1/auth/logout") || path.startsWith("/api/v1/auth/signIn") || path.startsWith("/api/v1/auth/refresh/token")) {

            filterChain.doFilter(request, response);

            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7).trim();

        if (jwt.isEmpty() || !jwtUtils.validateJwt(jwt)) {
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }


       String username = this.jwtUtils.getUsername(jwt);

      try{
          UserDetails user = this.userServiceSecurity.loadUserByUsername(username);

          UserModel userModel = this.userUseCase.findByUsername(user.getUsername());

          if(!userModel.getAvailable()){
              SecurityContextHolder.clearContext();
              response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
              return;
          }

          UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                  user,
                  null,
                  user.getAuthorities()
          );

          SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

      } catch (Exception e) {

          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

          return;
      }

      filterChain.doFilter(request, response);

    }
}
