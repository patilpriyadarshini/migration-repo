package com.modernized.dto;

public class LoginResponse {
    private String userId;
    private String userType;
    private String message;
    private boolean success;

    public LoginResponse() {}

    public LoginResponse(String userId, String userType, boolean success, String message) {
        this.userId = userId;
        this.userType = userType;
        this.success = success;
        this.message = message;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
}
