package com.medilabo.gateway.controller;

import com.medilabo.gateway.domain.AuthenticationRequest;
import com.medilabo.gateway.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling JWT authentication requests.
 */
@Slf4j
@RestController
public class JwtController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Endpoint for authenticating users and generating JWT tokens.
     * @param authenticationRequest The authentication request containing username and password.
     * @return The generated JWT token.
     * @throws Exception If authentication fails.
     */
    @RequestMapping(value = "/auth/generateToken", method = RequestMethod.POST)
    public String createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        log.info("Authentication attempt for user: {}", authenticationRequest.getUsername());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            log.error("Authentication failed for user: {} - {}", authenticationRequest.getUsername(), e.getMessage(), e);
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        String token = jwtUtil.generateToken(userDetails);
        log.info("Token successfully generated for user: {}", authenticationRequest.getUsername());
        return token;
    }
}