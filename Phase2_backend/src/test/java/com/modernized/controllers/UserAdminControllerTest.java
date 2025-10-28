package com.modernized.controllers;

import com.modernized.dto.UserCreateRequest;
import com.modernized.dto.UserResponse;
import com.modernized.dto.UserUpdateRequest;
import com.modernized.entities.User;
import com.modernized.repositories.UserRepository;
import com.modernized.utils.EntityMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserAdminController.class)
class UserAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private EntityMapper entityMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getUsers_ShouldReturnPagedUsers() throws Exception {
        User user = createTestUser();
        UserResponse response = createTestUserResponse();
        Page<User> userPage = new PageImpl<>(Arrays.asList(user), PageRequest.of(0, 10), 1);

        when(userRepository.findAll(any(PageRequest.class))).thenReturn(userPage);
        when(entityMapper.mapToUserResponse(any(User.class))).thenReturn(response);

        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void createUser_ShouldReturnCreatedUser_WhenValidRequest() throws Exception {
        User user = createTestUser();
        UserResponse response = createTestUserResponse();
        UserCreateRequest request = createTestCreateRequest();

        when(userRepository.findById(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(entityMapper.mapToUserResponse(any(User.class))).thenReturn(response);

        mockMvc.perform(post("/api/admin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("testuser"));
    }

    @Test
    void deleteUser_ShouldReturnSuccess_WhenUserExists() throws Exception {
        User user = createTestUser();

        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

        mockMvc.perform(delete("/api/admin/users/testuser"))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));
    }

    private User createTestUser() {
        User user = new User();
        user.setSecUsrId("testuser");
        user.setSecUsrFname("John");
        user.setSecUsrLname("Doe");
        user.setSecUsrType("ADMIN");
        return user;
    }

    private UserResponse createTestUserResponse() {
        UserResponse response = new UserResponse();
        response.setUserId("testuser");
        response.setFirstName("John");
        response.setLastName("Doe");
        response.setUserType("ADMIN");
        return response;
    }

    private UserCreateRequest createTestCreateRequest() {
        UserCreateRequest request = new UserCreateRequest();
        request.setUserId("testuser");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassword("password123");
        request.setUserType("ADMIN");
        return request;
    }
}
