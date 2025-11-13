package com.example.sennova.application.usecases;

import com.example.sennova.application.dto.authDto.LoginRequestDto;
import com.example.sennova.application.dto.authDto.LoginResponseDto;

public interface AuthUseCase {
    LoginResponseDto login(LoginRequestDto loginRequestDto);
   
}
