package com.example.sennova.domain.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
public class UserModel {
    private Long userId;
    private String name;
    private String email;
    private Long phoneNumber;
    private Long dni;
    private String imageProfile;
    private String position;
    private String refreshToken;
    private String username;
    private Boolean available;
    private String password;
    private RoleModel role;
    private LocalDate createAt;

    // preferences
    private boolean notifyEquipment;

    private boolean notifyReagents;

    private boolean notifyQuotes;

    private boolean notifyResults;

    private LocalDateTime lastSession;
}
