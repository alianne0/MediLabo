package com.medilabo.demographics.controller;

import com.medilabo.demographics.domain.Users;
import com.medilabo.demographics.repository.UserRepository;
import com.medilabo.demographics.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtil jwtUtils;
    @PostMapping("/signin")
    public String authenticateUser(@RequestBody Users users) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        users.getUsername(),
                        users.getPassword()
                )
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return jwtUtils.generateToken(userDetails.getUsername());
    }
    @PostMapping("/signup")
    public String registerUser(@RequestBody Users users) {
        if (userRepository.existsByUsername(users.getUsername())) {
            return "Error: Username is already taken!";
        }
        // Create new user's account

        Users newUser = new Users(
                users.getUsername(),
                encoder.encode(users.getPassword())
        );

        userRepository.save(newUser);
        return "User registered successfully!";
    }
}