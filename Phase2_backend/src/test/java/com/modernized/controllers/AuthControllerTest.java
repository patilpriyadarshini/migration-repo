package com.modernized.controllers;

import com.modernized.dto.LoginRequest;
import com.modernized.entities.User;
import com.modernized.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setSecUsrId("testuser");
        testUser.setSecUsrPwd("password123");
        testUser.setSecUsrType("R");
        testUser.setSecUsrFname("John");
        testUser.setSecUsrLname("Doe");
    }

    @Test
    void login_ShouldReturnSuccess_WhenValidCredentials() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserId("testuser");
        loginRequest.setPassword("password123");

        when(userRepository.findById("testuser")).thenReturn(Optional.of(testUser));

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("testuser"))
                .andExpect(jsonPath("$.userType").value("R"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Login successful"));
    }

    @Test
    void login_ShouldReturnFailure_WhenUserNotFound() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserId("nonexistent");
        loginRequest.setPassword("password123");

        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("nonexistent"))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid User ID or Password"));
    }

    @Test
    void login_ShouldReturnFailure_WhenWrongPassword() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserId("testuser");
        loginRequest.setPassword("wrongpassword");

        when(userRepository.findById("testuser")).thenReturn(Optional.of(testUser));

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("testuser"))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid User ID or Password"));
    }
}
