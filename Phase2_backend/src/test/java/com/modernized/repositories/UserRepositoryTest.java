package com.modernized.repositories;

import com.modernized.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findById_ShouldReturnUser_WhenUserExists() {
        User user = createTestUser();
        userRepository.save(user);

        Optional<User> found = userRepository.findById("testuser");

        assertTrue(found.isPresent());
        assertEquals("testuser", found.get().getSecUsrId());
    }

    @Test
    void findBySecUsrIdContaining_ShouldReturnMatchingUsers() {
        User user = createTestUser();
        userRepository.save(user);

        Page<User> found = userRepository.findBySecUsrIdContaining("test", PageRequest.of(0, 10));

        assertEquals(1, found.getTotalElements());
        assertEquals("testuser", found.getContent().get(0).getSecUsrId());
    }

    @Test
    void save_ShouldPersistUser() {
        User user = createTestUser();

        User saved = userRepository.save(user);

        assertNotNull(saved);
        assertEquals("testuser", saved.getSecUsrId());
        assertEquals("John", saved.getSecUsrFname());
    }

    private User createTestUser() {
        User user = new User();
        user.setSecUsrId("testuser");
        user.setSecUsrFname("John");
        user.setSecUsrLname("Doe");
        user.setSecUsrType("A");
        user.setSecUsrPwd("pass123");
        return user;
    }
}
