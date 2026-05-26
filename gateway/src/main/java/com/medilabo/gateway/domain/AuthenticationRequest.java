package com.medilabo.gateway.domain;

import lombok.Data;
/**
 * Data class representing an authentication request containing username and password.
 */
@Data
public class AuthenticationRequest {
    private String username;
    private String password;
}
