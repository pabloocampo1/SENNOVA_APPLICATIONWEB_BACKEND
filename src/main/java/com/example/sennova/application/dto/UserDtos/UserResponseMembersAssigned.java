package com.example.sennova.application.dto.UserDtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseMembersAssigned {
   private Long userId;
   private  String name;
    boolean available;
    String imageProfile;
    String email;
}
