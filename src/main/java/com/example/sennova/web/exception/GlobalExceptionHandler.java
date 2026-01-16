package com.example.sennova.web.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Error de validación", errors),
                HttpStatus.BAD_REQUEST
        );
    }

    // ✅ Errores de argumentos inválidos
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex){
        Map<String, String> errors = new HashMap<>();
        errors.put("general", ex.getMessage());

        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Error en la solicitud", errors),
                HttpStatus.BAD_REQUEST
        );
    }


    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e){
        Map<String, String> errors = new HashMap<>();
          return new ResponseEntity<>(
                  new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage(),errors ), HttpStatus.NOT_FOUND
          )     ;
    }

    // ✅ Errores de integridad (campos únicos, constraints)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        String message = "Violación de integridad de datos.";

        Throwable rootCause = ex.getMostSpecificCause();

        if (rootCause != null && rootCause.getMessage() != null) {
            String causeMessage = rootCause.getMessage();

            if (causeMessage.contains("internal_code")) {
                errors.put("internalCode", "El código interno ya existe, debe ser único.");
            } else if (causeMessage.contains("serial_number")) {
                errors.put("serialNumber", "El número de serie ya existe, debe ser único.");
            } else if (causeMessage.contains("Duplicate entry")) {
                message = "El valor que intentas guardar ya existe y debe ser único.";
            } else if (causeMessage.contains("constraint")) {
                message = "Se violó una restricción de la base de datos.";
            }
        }

        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.CONFLICT.value(), message, errors.isEmpty() ? null : errors),
                HttpStatus.CONFLICT
        );
    }

    // ✅ Recurso no encontrado
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("general", ex.getMessage());

        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Recurso no encontrado", errors),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());

        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error inesperado en el servidor", errors),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("username", ex.getMessage());

        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Usuario no encontrado", errors),
                HttpStatus.NOT_FOUND
        );
    }

    // ✅ Credenciales inválidas
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("credentials", "Usuario o contraseña incorrectos");

        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Error de autenticación", errors),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException authenticationException){
        Map<String, String> errors = new HashMap<>();
        errors.put("general", "Error de autenticacion");

        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Error de autenticación", errors),
                HttpStatus.UNAUTHORIZED
        );
    }

}
