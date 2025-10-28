package com.modernized.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modernized.dto.LoginRequest;
import com.modernized.entities.User;
import com.modernized.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthenticationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void loginFlow_ShouldWork_EndToEnd() throws Exception {
        User user = new User();
        user.setSecUsrId("integrt1");
        user.setSecUsrPwd("testpas1");
        user.setSecUsrType("A");
        user.setSecUsrFname("Integration");
        user.setSecUsrLname("Test");
        userRepository.save(user);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserId("integrt1");
        loginRequest.setPassword("testpas1");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.userId").value("integrt1"))
                .andExpect(jsonPath("$.userType").value("A"));
    }
}
