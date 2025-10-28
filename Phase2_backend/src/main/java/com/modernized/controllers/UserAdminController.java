package com.modernized.controllers;

import com.modernized.dto.UserResponse;
import com.modernized.dto.UserCreateRequest;
import com.modernized.dto.UserUpdateRequest;
import com.modernized.dto.PagedResponse;
import com.modernized.entities.User;
import com.modernized.repositories.UserRepository;
import com.modernized.utils.ResponseMapper;
import com.modernized.controllers.GlobalExceptionHandler.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * User Administration Controller (Admin Only)
 * Handles user management based on SCREEN-014 (User List), SCREEN-015 (Add User),
 * SCREEN-016 (Update User), and SCREEN-017 (Delete User)
 * Provides comprehensive user administration capabilities
 */
@RestController
@RequestMapping("/api/admin/users")
public class UserAdminController {

    private final UserRepository userRepository;

    public UserAdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Get Paginated User List
     * GET /api/admin/users
     * 
     * Displays paginated list of system users with optional filtering.
     * Based on SCREEN-014 (User List) with 10 users per page.
     * 
     * @param userId Optional user ID filter
     * @param page Page number (0-based)
     * @param size Page size (default 10 as per screen flow)
     * @return PagedResponse with user list
     */
    @GetMapping
    public ResponseEntity<PagedResponse<UserResponse>> getUsers(
            @RequestParam(required = false) String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage;
        
        if (userId != null) {
            userPage = userRepository.findBySecUsrIdContaining(userId, pageable);
        } else {
            userPage = userRepository.findAll(pageable);
        }
        
        List<UserResponse> userResponses = userPage.getContent().stream()
                .map(ResponseMapper::mapToUserResponse)
                .collect(Collectors.toList());
        
        PagedResponse<UserResponse> response = new PagedResponse<>(
                userResponses,
                userPage.getNumber(),
                userPage.getSize(),
                userPage.getTotalElements()
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * Get Individual User
     * GET /api/admin/users/{userId}
     * 
     * Retrieves individual user data for update operations.
     * Based on SCREEN-016 (Update User) functionality.
     * 
     * @param userId User ID to retrieve
     * @return UserResponse with user details
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isEmpty()) {
            throw new EntityNotFoundException("User not found");
        }
        
        User user = userOpt.get();
        UserResponse response = ResponseMapper.mapToUserResponse(user);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Create New User
     * POST /api/admin/users
     * 
     * Creates new system users with role assignment.
     * Based on SCREEN-015 (Add User) functionality.
     * 
     * @param createRequest User creation data
     * @return UserResponse with created user details
     */
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest createRequest) {
        Optional<User> existingUser = userRepository.findById(createRequest.getUserId());
        
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("User ID already exists");
        }
        
        User user = new User();
        user.setSecUsrId(createRequest.getUserId());
        user.setSecUsrFname(createRequest.getFirstName());
        user.setSecUsrLname(createRequest.getLastName());
        user.setSecUsrPwd(createRequest.getPassword());
        user.setSecUsrType(createRequest.getUserType());
        
        User savedUser = userRepository.save(user);
        UserResponse response = ResponseMapper.mapToUserResponse(savedUser);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Update User Information
     * PUT /api/admin/users/{userId}
     * 
     * Updates existing user information and permissions.
     * Based on SCREEN-016 (Update User) functionality.
     * 
     * @param userId User ID to update
     * @param updateRequest Updated user data
     * @return UserResponse with updated information
     */
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable String userId,
            @Valid @RequestBody UserUpdateRequest updateRequest) {
        
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isEmpty()) {
            throw new EntityNotFoundException("User not found");
        }
        
        User user = userOpt.get();
        user.setSecUsrFname(updateRequest.getFirstName());
        user.setSecUsrLname(updateRequest.getLastName());
        user.setSecUsrPwd(updateRequest.getPassword());
        user.setSecUsrType(updateRequest.getUserType());
        
        User savedUser = userRepository.save(user);
        UserResponse response = ResponseMapper.mapToUserResponse(savedUser);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Delete User
     * DELETE /api/admin/users/{userId}
     * 
     * Removes users from the system with confirmation.
     * Based on SCREEN-017 (Delete User) functionality.
     * 
     * @param userId User ID to delete
     * @return Success message
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable String userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isEmpty()) {
            throw new EntityNotFoundException("User not found");
        }
        
        userRepository.deleteById(userId);
        
        return ResponseEntity.ok("User deleted successfully");
    }

    /**
     * Get User Details for Deletion Confirmation
     * GET /api/admin/users/{userId}/details
     * 
     * Retrieves user data for deletion confirmation display.
     * Based on SCREEN-017 (Delete User) confirmation functionality.
     * 
     * @param userId User ID to view
     * @return UserResponse with user details
     */
    @GetMapping("/{userId}/details")
    public ResponseEntity<UserResponse> getUserForDeletion(@PathVariable String userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isEmpty()) {
            throw new EntityNotFoundException("User not found");
        }
        
        User user = userOpt.get();
        UserResponse response = ResponseMapper.mapToUserResponse(user);
        
        return ResponseEntity.ok(response);
    }

}
