package com.modernized.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modernized.dto.UserCreateRequest;
import com.modernized.dto.UserUpdateRequest;
import com.modernized.entities.User;
import com.modernized.repositories.UserRepository;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserAdminController.class)
class UserAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getUsers_ShouldReturnPagedUsers() throws Exception {
        User user = createTestUser();
        Page<User> userPage = new PageImpl<>(Arrays.asList(user), PageRequest.of(0, 10), 1);
        when(userRepository.findAll(any(PageRequest.class))).thenReturn(userPage);

        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].userId").value("testuser"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getUser_ShouldReturnUser_WhenUserExists() throws Exception {
        User user = createTestUser();
        when(userRepository.findById("testuser")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/admin/users/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("testuser"))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void createUser_ShouldCreateUser_WhenValidRequest() throws Exception {
        User user = createTestUser();
        UserCreateRequest request = createTestCreateRequest();
        
        when(userRepository.findById("newuser1")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/admin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("testuser"));
    }

    @Test
    void createUser_ShouldReturnError_WhenUserExists() throws Exception {
        User user = createTestUser();
        UserCreateRequest request = createTestCreateRequest();
        
        when(userRepository.findById("newuser1")).thenReturn(Optional.of(user));

        mockMvc.perform(post("/api/admin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_ShouldUpdateUser_WhenValidRequest() throws Exception {
        User user = createTestUser();
        UserUpdateRequest request = createTestUpdateRequest();
        
        when(userRepository.findById("testuser")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        mockMvc.perform(put("/api/admin/users/testuser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("testuser"));
    }

    @Test
    void deleteUser_ShouldDeleteUser_WhenUserExists() throws Exception {
        User user = createTestUser();
        when(userRepository.findById("testuser")).thenReturn(Optional.of(user));

        mockMvc.perform(delete("/api/admin/users/testuser"))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));
    }

    private User createTestUser() {
        User user = new User();
        user.setSecUsrId("testuser");
        user.setSecUsrFname("John");
        user.setSecUsrLname("Doe");
        user.setSecUsrType("A");
        user.setSecUsrPwd("pass1234");
        return user;
    }

    private UserCreateRequest createTestCreateRequest() {
        UserCreateRequest request = new UserCreateRequest();
        request.setUserId("newuser1");
        request.setFirstName("Jane");
        request.setLastName("Smith");
        request.setPassword("newpass1");
        request.setUserType("U");
        return request;
    }

    private UserUpdateRequest createTestUpdateRequest() {
        UserUpdateRequest request = new UserUpdateRequest();
        request.setFirstName("Jane");
        request.setLastName("Smith");
        request.setPassword("newpass1");
        request.setUserType("U");
        return request;
    }
}
