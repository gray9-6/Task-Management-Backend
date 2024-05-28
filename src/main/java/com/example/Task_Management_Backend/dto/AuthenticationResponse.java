package com.example.Task_Management_Backend.dto;

import com.example.Task_Management_Backend.enums.UserRole;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationResponse {
    String jwt;
    Long userId;
    UserRole userRole;
}
