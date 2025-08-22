package com.modernized.controllers;

import com.modernized.entities.User;
import com.modernized.repositories.UserRepository;
import com.modernized.utils.BaseControllerTest;
import com.modernized.utils.TestDataBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest extends BaseControllerTest {

    @MockBean
    private UserRepository userRepository;

    @Test
    void login_ShouldReturnSuccess_WhenValidCredentials() throws Exception {
        User user = TestDataBuilder.createTestUser();
        when(userRepository.findById("TESTUSER")).thenReturn(Optional.of(user));

        String loginRequest = asJsonString(TestDataBuilder.createLoginRequest());

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("TESTUSER"))
                .andExpect(jsonPath("$.userType").value("U"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Login successful"));
    }

    @Test
    void login_ShouldReturnFailure_WhenUserNotFound() throws Exception {
        when(userRepository.findById("INVALID1")).thenReturn(Optional.empty());

        String loginRequest = "{\"userId\":\"INVALID1\",\"password\":\"password\"}";

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid User ID or Password"));
    }

    @Test
    void login_ShouldReturnFailure_WhenInvalidPassword() throws Exception {
        User user = TestDataBuilder.createTestUser();
        when(userRepository.findById("TESTUSER")).thenReturn(Optional.of(user));

        String loginRequest = "{\"userId\":\"TESTUSER\",\"password\":\"wrongpwd\"}";

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid User ID or Password"));
    }
}
