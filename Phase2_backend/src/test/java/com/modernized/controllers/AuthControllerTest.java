package com.modernized.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modernized.dto.LoginRequest;
import com.modernized.entities.User;
import com.modernized.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

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

    @Test
    void login_ShouldReturnSuccess_WhenValidCredentials() throws Exception {
        User user = createTestUser();
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserId("testuser");
        loginRequest.setPassword("pass1234");

        when(userRepository.findById("testuser")).thenReturn(Optional.of(user));

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("testuser"))
                .andExpect(jsonPath("$.userType").value("A"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Login successful"));
    }

    @Test
    void login_ShouldReturnFailure_WhenUserNotFound() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserId("noexist1");
        loginRequest.setPassword("pass1234");

        when(userRepository.findById("noexist1")).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid User ID or Password"));
    }

    @Test
    void login_ShouldReturnFailure_WhenInvalidPassword() throws Exception {
        User user = createTestUser();
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserId("testuser");
        loginRequest.setPassword("wrongpwd");

        when(userRepository.findById("testuser")).thenReturn(Optional.of(user));

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid User ID or Password"));
    }

    private User createTestUser() {
        User user = new User();
        user.setSecUsrId("testuser");
        user.setSecUsrPwd("pass1234");
        user.setSecUsrType("A");
        user.setSecUsrFname("Test");
        user.setSecUsrLname("User");
        return user;
    }
}
