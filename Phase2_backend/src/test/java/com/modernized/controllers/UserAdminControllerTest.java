package com.modernized.controllers;

import com.modernized.dto.UserResponse;
import com.modernized.dto.UserCreateRequest;
import com.modernized.dto.UserUpdateRequest;
import com.modernized.dto.PagedResponse;
import com.modernized.entities.User;
import com.modernized.repositories.UserRepository;
import com.modernized.utils.EntityMapper;
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
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
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

    private User testUser;
    private UserResponse testUserResponse;
    private UserCreateRequest testCreateRequest;
    private UserUpdateRequest testUpdateRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setSecUsrId("user1234");
        testUser.setSecUsrFname("John");
        testUser.setSecUsrLname("Doe");
        testUser.setSecUsrPwd("password123");
        testUser.setSecUsrType("A");

        testUserResponse = new UserResponse();
        testUserResponse.setUserId("user1234");
        testUserResponse.setFirstName("John");
        testUserResponse.setLastName("Doe");
        testUserResponse.setUserType("A");

        testCreateRequest = new UserCreateRequest();
        testCreateRequest.setUserId("user1234");
        testCreateRequest.setFirstName("John");
        testCreateRequest.setLastName("Doe");
        testCreateRequest.setPassword("pass1234");
        testCreateRequest.setUserType("A");

        testUpdateRequest = new UserUpdateRequest();
        testUpdateRequest.setFirstName("Jane");
        testUpdateRequest.setLastName("Smith");
        testUpdateRequest.setPassword("newpass1");
        testUpdateRequest.setUserType("U");
    }

    @Test
    void getUsers_NoFilters_ReturnsPagedResponse() throws Exception {
        List<User> users = Arrays.asList(testUser);
        Page<User> userPage = new PageImpl<>(users, PageRequest.of(0, 10), 1);
        
        when(userRepository.findAll(any(PageRequest.class))).thenReturn(userPage);
        when(entityMapper.mapToUserResponse(any(User.class))).thenReturn(testUserResponse);

        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].userId").value("user1234"))
                .andExpect(jsonPath("$.pageNumber").value(0))
                .andExpect(jsonPath("$.pageSize").value(10))
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(userRepository).findAll(any(PageRequest.class));
        verify(entityMapper).mapToUserResponse(testUser);
    }

    @Test
    void getUser_ValidUserId_ReturnsUserResponse() throws Exception {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(testUser));
        when(entityMapper.mapToUserResponse(any(User.class))).thenReturn(testUserResponse);

        mockMvc.perform(get("/api/admin/users/user1234"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value("user1234"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.userType").value("A"));

        verify(userRepository).findById("user1234");
        verify(entityMapper).mapToUserResponse(testUser);
    }

    @Test
    void getUser_InvalidUserId_ThrowsEntityNotFoundException() throws Exception {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/admin/users/invalid"))
                .andExpect(status().isNotFound());

        verify(userRepository).findById("invalid");
        verify(entityMapper, never()).mapToUserResponse(any());
    }

    @Test
    void createUser_ValidRequest_ReturnsCreatedUser() throws Exception {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(entityMapper.mapToUserResponse(any(User.class))).thenReturn(testUserResponse);

        mockMvc.perform(post("/api/admin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCreateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value("user1234"));

        verify(userRepository).findById("user1234");
        verify(userRepository).save(any(User.class));
        verify(entityMapper).mapToUserResponse(testUser);
    }

    @Test
    void createUser_ExistingUserId_ThrowsIllegalArgumentException() throws Exception {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(testUser));

        mockMvc.perform(post("/api/admin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCreateRequest)))
                .andExpect(status().isBadRequest());

        verify(userRepository).findById("user1234");
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_ValidRequest_ReturnsUpdatedUser() throws Exception {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(entityMapper.mapToUserResponse(any(User.class))).thenReturn(testUserResponse);

        mockMvc.perform(put("/api/admin/users/user1234")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value("user1234"));

        verify(userRepository).findById("user1234");
        verify(userRepository).save(testUser);
        verify(entityMapper).mapToUserResponse(testUser);
    }

    @Test
    void deleteUser_ValidUserId_ReturnsSuccessMessage() throws Exception {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(testUser));

        mockMvc.perform(delete("/api/admin/users/user1234"))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));

        verify(userRepository).findById("user1234");
        verify(userRepository).deleteById("user1234");
    }

    @Test
    void deleteUser_InvalidUserId_ThrowsEntityNotFoundException() throws Exception {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/admin/users/invalid"))
                .andExpect(status().isNotFound());

        verify(userRepository).findById("invalid");
        verify(userRepository, never()).deleteById(any());
    }
}
