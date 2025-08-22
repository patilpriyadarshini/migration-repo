package com.modernized.controllers;

import com.modernized.dto.LoginRequest;
import com.modernized.dto.LoginResponse;
import com.modernized.entities.User;
import com.modernized.repositories.UserRepository;
import com.modernized.services.PasswordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PasswordService passwordService;
    
    private AuthController authController;

    @BeforeEach
    void setUp() {
        authController = new AuthController(userRepository, passwordService);
    }

    @Test
    void testSuccessfulLogin() {
        LoginRequest loginRequest = new LoginRequest("TESTUSER", "password");
        User user = new User("TESTUSER", "John", "Doe", "hashedpwd", "A");
        
        when(userRepository.findById("TESTUSER")).thenReturn(Optional.of(user));
        when(passwordService.verifyPassword("password", "hashedpwd")).thenReturn(true);
        
        ResponseEntity<LoginResponse> response = authController.login(loginRequest);
        
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isSuccess());
        assertEquals("A", response.getBody().getUserType());
    }

    @Test
    void testFailedLoginInvalidUser() {
        LoginRequest loginRequest = new LoginRequest("INVALID", "password");
        
        when(userRepository.findById("INVALID")).thenReturn(Optional.empty());
        
        ResponseEntity<LoginResponse> response = authController.login(loginRequest);
        
        assertEquals(200, response.getStatusCodeValue());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Invalid User ID or Password", response.getBody().getMessage());
    }

    @Test
    void testFailedLoginInvalidPassword() {
        LoginRequest loginRequest = new LoginRequest("TESTUSER", "wrongpass");
        User user = new User("TESTUSER", "John", "Doe", "hashedpwd", "A");
        
        when(userRepository.findById("TESTUSER")).thenReturn(Optional.of(user));
        when(passwordService.verifyPassword("wrongpass", "hashedpwd")).thenReturn(false);
        
        ResponseEntity<LoginResponse> response = authController.login(loginRequest);
        
        assertEquals(200, response.getStatusCodeValue());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Invalid User ID or Password", response.getBody().getMessage());
    }
}
