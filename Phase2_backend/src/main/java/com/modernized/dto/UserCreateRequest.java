package com.modernized.dto;

import jakarta.validation.constraints.*;

public class UserCreateRequest {
    
    @NotBlank(message = "First name cannot be empty")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty")
    private String lastName;

    @NotBlank(message = "User ID cannot be empty")
    @Size(min = 8, max = 8, message = "User ID must be exactly 8 characters")
    private String userId;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, max = 8, message = "Password must be exactly 8 characters")
    private String password;

    @NotBlank(message = "User type cannot be empty")
    @Pattern(regexp = "[AU]", message = "User type must be A (Admin) or U (User)")
    private String userType;

    public UserCreateRequest() {}

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }
}
