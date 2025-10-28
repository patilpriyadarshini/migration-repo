package com.modernized.controllers;

import com.modernized.dto.UserCreateRequest;
import com.modernized.dto.UserUpdateRequest;
import com.modernized.entities.User;
import com.modernized.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setSecUsrId("testuser");
        testUser.setSecUsrFname("John");
        testUser.setSecUsrLname("Doe");
        testUser.setSecUsrPwd("password123");
        testUser.setSecUsrType("R");
    }

    @Test
    void getUsers_ShouldReturnPagedUsers_WhenNoFilters() throws Exception {
        Page<User> userPage = new PageImpl<>(Arrays.asList(testUser), PageRequest.of(0, 10), 1);
        when(userRepository.findAll(any(PageRequest.class))).thenReturn(userPage);

        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].userId").value("testuser"))
                .andExpect(jsonPath("$.content[0].firstName").value("John"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getUser_ShouldReturnUser_WhenUserExists() throws Exception {
        when(userRepository.findById("testuser")).thenReturn(Optional.of(testUser));

        mockMvc.perform(get("/api/admin/users/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("testuser"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.userType").value("R"));
    }

    @Test
    void getUser_ShouldReturnNotFound_WhenUserDoesNotExist() throws Exception {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/admin/users/nonexistent"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createUser_ShouldReturnCreatedUser_WhenValidRequest() throws Exception {
        UserCreateRequest createRequest = new UserCreateRequest();
        createRequest.setUserId("newuser");
        createRequest.setFirstName("Jane");
        createRequest.setLastName("Smith");
        createRequest.setPassword("newpassword");
        createRequest.setUserType("R");

        User newUser = new User();
        newUser.setSecUsrId("newuser");
        newUser.setSecUsrFname("Jane");
        newUser.setSecUsrLname("Smith");
        newUser.setSecUsrPwd("newpassword");
        newUser.setSecUsrType("R");

        when(userRepository.findById("newuser")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        mockMvc.perform(post("/api/admin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("newuser"))
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Smith"));
    }

    @Test
    void createUser_ShouldReturnBadRequest_WhenUserAlreadyExists() throws Exception {
        UserCreateRequest createRequest = new UserCreateRequest();
        createRequest.setUserId("testuser");
        createRequest.setFirstName("Jane");
        createRequest.setLastName("Smith");
        createRequest.setPassword("newpassword");
        createRequest.setUserType("R");

        when(userRepository.findById("testuser")).thenReturn(Optional.of(testUser));

        mockMvc.perform(post("/api/admin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser_WhenValidRequest() throws Exception {
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setFirstName("Johnny");
        updateRequest.setLastName("Doe");
        updateRequest.setPassword("newpassword");
        updateRequest.setUserType("A");

        User updatedUser = new User();
        updatedUser.setSecUsrId("testuser");
        updatedUser.setSecUsrFname("Johnny");
        updatedUser.setSecUsrLname("Doe");
        updatedUser.setSecUsrPwd("newpassword");
        updatedUser.setSecUsrType("A");

        when(userRepository.findById("testuser")).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/admin/users/testuser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("testuser"))
                .andExpect(jsonPath("$.firstName").value("Johnny"));
    }

    @Test
    void deleteUser_ShouldReturnSuccess_WhenUserExists() throws Exception {
        when(userRepository.findById("testuser")).thenReturn(Optional.of(testUser));

        mockMvc.perform(delete("/api/admin/users/testuser"))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));
    }

    @Test
    void deleteUser_ShouldReturnNotFound_WhenUserDoesNotExist() throws Exception {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/admin/users/nonexistent"))
                .andExpect(status().isNotFound());
    }
}
