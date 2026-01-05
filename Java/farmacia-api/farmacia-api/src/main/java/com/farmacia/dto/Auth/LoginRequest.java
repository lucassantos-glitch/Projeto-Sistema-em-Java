package com.farmacia.dto.Auth;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
