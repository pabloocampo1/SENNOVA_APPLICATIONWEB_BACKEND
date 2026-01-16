package com.example.sennova.application.dto.authDto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

// this method is used for change the password in the logi
@Data
public class ChangePasswordLoginDto {
    @NotBlank(message = "The token is required")
    private String token;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "The password must be at least 8 characters long")
    private String newPassword;

    
}
