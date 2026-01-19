package com.example.sennova.application.dto.UserDtos;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserUpdateProfileRequest {
    private Long userId;
    private String name;
    private String jobPosition;
    private Long phoneNumber;
    private MultipartFile image;
}
