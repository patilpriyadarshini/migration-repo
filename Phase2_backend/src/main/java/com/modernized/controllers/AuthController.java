package com.modernized.controllers;

import com.modernized.dto.LoginRequest;
import com.modernized.dto.LoginResponse;
import com.modernized.entities.User;
import com.modernized.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.Optional;

/**
 * Authentication Controller
 * Handles user login authentication based on SCREEN-001 (User Authentication)
 * Routes users to appropriate menu based on user type (Admin vs Regular)
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * User Login Authentication
     * POST /api/auth/login
     * 
     * Authenticates user credentials against USRSEC file and returns user type
     * for appropriate menu routing.
     * 
     * @param loginRequest User credentials (8-char User ID and Password)
     * @return LoginResponse with user type and authentication status
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        Optional<User> userOpt = userRepository.findById(loginRequest.getUserId());
        
        if (userOpt.isEmpty()) {
            return ResponseEntity.ok(new LoginResponse(
                loginRequest.getUserId(), 
                null, 
                false, 
                "Invalid User ID or Password"
            ));
        }
        
        User user = userOpt.get();
        
        if (!user.getSecUsrPwd().equals(loginRequest.getPassword())) {
            return ResponseEntity.ok(new LoginResponse(
                loginRequest.getUserId(), 
                null, 
                false, 
                "Invalid User ID or Password"
            ));
        }
        
        return ResponseEntity.ok(new LoginResponse(
            user.getSecUsrId(),
            user.getSecUsrType(),
            true,
            "Login successful"
        ));
    }
}
