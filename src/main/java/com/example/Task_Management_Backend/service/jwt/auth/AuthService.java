package com.example.Task_Management_Backend.service.jwt.auth;

import com.example.Task_Management_Backend.dto.SignupRequestDto;
import com.example.Task_Management_Backend.dto.UserDto;

public interface AuthService {

    UserDto signupUser(SignupRequestDto signupRequestDto);

    boolean hasUserWithEmail(String email);
}
