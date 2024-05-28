package com.example.Task_Management_Backend.service.jwt.auth;

import com.example.Task_Management_Backend.dto.SignupRequestDto;
import com.example.Task_Management_Backend.dto.UserDto;
import com.example.Task_Management_Backend.entity.User;
import com.example.Task_Management_Backend.enums.UserRole;
import com.example.Task_Management_Backend.repository.UserRepository;
import com.example.Task_Management_Backend.transformer.UserTransformer;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;

    @PostConstruct
    public void createAdminAccount(){
        Optional<User> optionalUser = userRepository.findByUserRole(UserRole.ADMIN);

        if(optionalUser.isPresent()){
            log.info("Admin Account is already exists");
        }else{
            User adminAccount = User.builder()
                    .name("admin")
                    .email("admin@gmail.com")
                    .password(new BCryptPasswordEncoder().encode("admin"))
                    .userRole(UserRole.ADMIN)
                    .build();

            userRepository.save(adminAccount);

            log.info("Admin Account Created successfully !!");
        }


    }

    @Override
    public UserDto signupUser(SignupRequestDto signupRequestDto) {
        // create the user from the request dto
        User createdUser = UserTransformer.signupRequestDtoToUser(signupRequestDto);
        createdUser.setPassword(new BCryptPasswordEncoder().encode(signupRequestDto.getPassword()));

        // save the user
        User savedUser = userRepository.save(createdUser);

        // convert the saved user to UserDto and return it
        return UserTransformer.userToUserDto(savedUser);
    }

    @Override
    public boolean hasUserWithEmail(String email) {
        return userRepository.findFirstByEmail(email).isPresent();
    }
}
