package com.example.Task_Management_Backend.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    Long id;
    String name;
    String email;
    String password;
}
