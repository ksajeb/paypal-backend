package com.paypal.user_service.controller;

import com.paypal.user_service.dto.JWTResponseDto;
import com.paypal.user_service.dto.LoginDto;
import com.paypal.user_service.dto.SignupDto;
import com.paypal.user_service.entity.User;
import com.paypal.user_service.repository.UserRepository;
import com.paypal.user_service.util.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JWTUtils jwtUtils;


    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupDto signupDto){
        Optional<User> existingUser=userRepository.findByEmail(signupDto.getEmail());
        if(existingUser.isPresent()){
            return ResponseEntity.badRequest().body("User already exists!!");
        }

        User user=new User();
        user.setName(signupDto.getName());
        user.setEmail(signupDto.getEmail());
        user.setRole("ROLE_USER");
        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));
        User savedUser=userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){
        Optional<User> userOpt=userRepository.findByEmail(loginDto.getEmail());
        if(userOpt.isEmpty()){
            return ResponseEntity.badRequest().body("User not found.");
        }

        User user=userOpt.get();
        if(!passwordEncoder.matches(loginDto.getPassword(),user.getPassword())){
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        Map<String,Object> claims=new HashMap<>();
        claims.put("role",user.getRole());

        String token=jwtUtils.generateAccessToken(user.getId(),user.getEmail(),user.getRole());
        return ResponseEntity.ok(new JWTResponseDto(token));
    }
}
