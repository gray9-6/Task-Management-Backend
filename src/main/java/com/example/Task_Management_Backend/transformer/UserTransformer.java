package com.example.Task_Management_Backend.transformer;

import com.example.Task_Management_Backend.dto.SignupRequestDto;
import com.example.Task_Management_Backend.dto.UserDto;
import com.example.Task_Management_Backend.entity.User;
import com.example.Task_Management_Backend.enums.UserRole;

public class UserTransformer {

    public static User signupRequestDtoToUser(SignupRequestDto signupRequestDto){
        return User.builder()
                .name(signupRequestDto.getName())
                .email(signupRequestDto.getEmail())
                .userRole(UserRole.EMPLOYEE)
                .build();
    }

    public static UserDto userToUserDto(User user){
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }
}
