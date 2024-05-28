package com.example.Task_Management_Backend.controller.auth;

import com.example.Task_Management_Backend.dto.AuthenticationRequest;
import com.example.Task_Management_Backend.dto.AuthenticationResponse;
import com.example.Task_Management_Backend.dto.SignupRequestDto;
import com.example.Task_Management_Backend.dto.UserDto;
import com.example.Task_Management_Backend.entity.User;
import com.example.Task_Management_Backend.repository.UserRepository;
import com.example.Task_Management_Backend.service.jwt.UserService;
import com.example.Task_Management_Backend.service.jwt.auth.AuthService;
import com.example.Task_Management_Backend.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
//@CrossOrigin("*")  // we will allow all the request which is coming from the frontend
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;


    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody SignupRequestDto signupRequestDto){
        if(authService.hasUserWithEmail(signupRequestDto.getEmail())){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("User Already Exists with this email");
        }

        UserDto createdUser = authService.signupUser(signupRequestDto);
        if(createdUser == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User Not Created");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }


    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody AuthenticationRequest authenticationRequest){
        // authenticate the user which is coming
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getEmail(),
                    authenticationRequest.getPassword()
                    )
            );
        }catch (BadCredentialsException e){
            throw new BadCredentialsException("Incorrect username or password");
        }


        // if the user is authenticated successfully then get the user
        final UserDetails userDetails = userService.userDetailService().loadUserByUsername(authenticationRequest.getEmail());
        final String jwtToken = jwtUtil.generateToken(userDetails);

        // get the user from the db
        Optional<User> optionalUser = userRepository.findFirstByEmail(authenticationRequest.getEmail());

        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        if(optionalUser.isPresent()){
         authenticationResponse.setUserId(optionalUser.get().getId());
         authenticationResponse.setUserRole(optionalUser.get().getUserRole());
         authenticationResponse.setJwt(jwtToken);
        }

        return authenticationResponse;
    }


}
