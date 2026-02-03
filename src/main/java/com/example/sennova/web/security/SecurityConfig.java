package com.example.sennova.web.security;

import com.example.sennova.domain.constants.RoleConstantsNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final String ROLE_ADMIN = RoleConstantsNotification.ROLE_ADMIN;
    private final String ROLE_SUPERADMIN = RoleConstantsNotification.ROLE_SUPERADMIN;
    private final  String ROLE_ANALYST = RoleConstantsNotification.ROLE_ANALYST;
    private  JwtFilter jwtFilter;
    @Autowired
    private CorsConfig corsConfig;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.
                csrf(AbstractHttpConfigurer::disable).
                cors(httpSecurityCorsConfigurer -> {
                    httpSecurityCorsConfigurer.configurationSource(corsConfig.corsConfigurationSource());
                }).
                authorizeHttpRequests(request -> {


                            request.requestMatchers("/ws-notifications/**").permitAll();
                            // Auth request
                            request.requestMatchers(HttpMethod.POST, "/api/v1/auth/signIn").permitAll();
                            request.requestMatchers(HttpMethod.POST, "/api/v1/auth/refresh/token").permitAll();
                            request.requestMatchers(HttpMethod.POST, "/api/v1/auth/logout").permitAll();
                            request.requestMatchers(HttpMethod.POST, "/api/v1/auth/signIn/google").permitAll();
                            request.requestMatchers(HttpMethod.POST, "/api/v1/auth/password/reset").permitAll();
                            request.requestMatchers(HttpMethod.POST, "/api/v1/auth/password/reset-token").permitAll();
                            request.requestMatchers(HttpMethod.GET, "/api/v1/auth/password/reset-token/**").permitAll();

                            // users
//                            request.requestMatchers(HttpMethod.POST, "/api/v1/users/save").hasRole(ROLE_SUPERADMIN);
//                            request.requestMatchers(HttpMethod.GET, "/api/v1/users/getAll").hasAnyRole(ROLE_SUPERADMIN);
//                            request.requestMatchers(HttpMethod.GET, "/api/v1/users/getByName/**").hasAnyRole(ROLE_SUPERADMIN);
//                            request.requestMatchers(HttpMethod.GET, "/api/v1/users/getByDni/**").hasAnyRole(ROLE_SUPERADMIN);
//                            request.requestMatchers(HttpMethod.GET, "/api/v1/users/getByRole/**").hasAnyRole(ROLE_SUPERADMIN);
//                            request.requestMatchers(HttpMethod.PUT, "/api/v1/users/update/**").hasAnyRole(ROLE_SUPERADMIN, ROLE_ADMIN,  ROLE_ANALYST);
//                            request.requestMatchers(HttpMethod.DELETE, "/api/v1/users/delete/**").hasAnyRole(ROLE_SUPERADMIN);
//
//
//                            // CUSTOMERS
//                            request.requestMatchers(HttpMethod.GET, "/api/v1/customers/**").hasRole(ROLE_SUPERADMIN);
//                            request.requestMatchers(HttpMethod.DELETE, "/api/v1/customers/delete/**").hasRole(ROLE_SUPERADMIN);
//                            request.requestMatchers(HttpMethod.PUT, "/api/v1/customers/edit/**").hasRole(ROLE_SUPERADMIN);
//
//
//
//                            // products
//                            request.requestMatchers(HttpMethod.DELETE, "/api/v1/product/delete").hasAnyRole(ROLE_SUPERADMIN);
//                            request.requestMatchers(HttpMethod.GET, "/api/v1/product/getAll").hasAnyRole(ROLE_SUPERADMIN, ROLE_ANALYST);
//                            request.requestMatchers(HttpMethod.PUT, "/api/v1/product/update").hasAnyRole(ROLE_SUPERADMIN, ROLE_ANALYST);
//                            request.requestMatchers(HttpMethod.POST, "/api/v1/product/**").hasAnyRole(ROLE_SUPERADMIN, ROLE_ADMIN);
//                            request.requestMatchers(HttpMethod.GET, "/api/v1/product/**").permitAll();
//
//                            // location and usages
//                            request.requestMatchers(HttpMethod.DELETE, "/api/v1/location/**").hasRole(ROLE_SUPERADMIN);
//                            request.requestMatchers(HttpMethod.DELETE, "/api/v1/usage/**").hasRole(ROLE_SUPERADMIN);
//                            request.requestMatchers(HttpMethod.GET, "/api/v1/location/**").hasAnyRole(ROLE_SUPERADMIN, ROLE_ADMIN, ROLE_ANALYST);
//                            request.requestMatchers(HttpMethod.GET, "/api/v1/usage/**").hasAnyRole(ROLE_SUPERADMIN, ROLE_ADMIN, ROLE_ANALYST);
//
//
//
//                            // inventory
//                            request.requestMatchers(HttpMethod.GET, "/api/v1/equipment/**").hasAnyRole(ROLE_SUPERADMIN, ROLE_ADMIN, ROLE_ANALYST);
//                            request.requestMatchers(HttpMethod.POST, "/api/v1/equipment/**").hasAnyRole(ROLE_SUPERADMIN, ROLE_ADMIN, ROLE_ANALYST);
//                            request.requestMatchers(HttpMethod.DELETE, "/api/v1/equipment/**").hasRole(ROLE_SUPERADMIN);
//
//
//                            // test request
//                            request.requestMatchers(HttpMethod.GET, "/api/v1/testRequest/**").hasAnyRole(ROLE_SUPERADMIN, ROLE_ADMIN, ROLE_ANALYST);
//                             request.requestMatchers(HttpMethod.DELETE, "/api/v1/testRequest/**").hasRole(ROLE_SUPERADMIN);
//
//
//                             request.requestMatchers(HttpMethod.DELETE, "/api/v1/samples/**").hasRole(ROLE_SUPERADMIN);
//
//
//                            // dashboard
//                            request.requestMatchers(HttpMethod.GET,"/api/v1/dashboard/**").hasAnyRole(ROLE_SUPERADMIN, ROLE_ADMIN, ROLE_ANALYST);

                            request.anyRequest().authenticated();
                        }
                )
                .headers(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class).
                build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


}
