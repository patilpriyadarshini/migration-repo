package com.modernized.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequest {
    
    @NotBlank(message = "User ID cannot be empty")
    @Size(min = 8, max = 8, message = "User ID must be exactly 8 characters")
    private String userId;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, max = 8, message = "Password must be exactly 8 characters")
    private String password;

    public LoginRequest() {}

    public LoginRequest(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
