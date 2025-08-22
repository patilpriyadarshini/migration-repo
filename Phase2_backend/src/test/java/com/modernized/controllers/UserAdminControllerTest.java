package com.modernized.controllers;

import com.modernized.entities.User;
import com.modernized.repositories.UserRepository;
import com.modernized.utils.BaseControllerTest;
import com.modernized.utils.TestDataBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserAdminController.class)
class UserAdminControllerTest extends BaseControllerTest {

    @MockBean
    private UserRepository userRepository;

    @Test
    void getUsers_ShouldReturnPagedUsers() throws Exception {
        User user = TestDataBuilder.createTestUser();
        Page<User> userPage = new PageImpl<>(Arrays.asList(user), PageRequest.of(0, 10), 1);

        when(userRepository.findAll(any(PageRequest.class))).thenReturn(userPage);

        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].userId").value("TESTUSER"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void createUser_ShouldCreateUser_WhenValidRequest() throws Exception {
        User user = TestDataBuilder.createTestUser();

        when(userRepository.findById("NEWUSER1")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        String createRequest = "{\"userId\":\"NEWUSER1\",\"firstName\":\"New\",\"lastName\":\"User\",\"password\":\"password\",\"userType\":\"U\"}";

        mockMvc.perform(post("/api/admin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").exists());
    }

    @Test
    void deleteUser_ShouldDeleteUser_WhenUserExists() throws Exception {
        User user = TestDataBuilder.createTestUser();

        when(userRepository.findById("TESTUSER")).thenReturn(Optional.of(user));

        mockMvc.perform(delete("/api/admin/users/TESTUSER"))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));
    }
}
