package com.modernized.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PasswordServiceTest {

    private PasswordService passwordService;

    @BeforeEach
    void setUp() {
        passwordService = new PasswordService();
    }

    @Test
    void testPasswordHashing() {
        String plainPassword = "testpass";
        String hashedPassword = passwordService.hashPassword(plainPassword);
        
        assertNotNull(hashedPassword);
        assertNotEquals(plainPassword, hashedPassword);
        assertTrue(hashedPassword.startsWith("$2a$12$"));
    }

    @Test
    void testPasswordVerification() {
        String plainPassword = "testpass";
        String hashedPassword = passwordService.hashPassword(plainPassword);
        
        assertTrue(passwordService.verifyPassword(plainPassword, hashedPassword));
        assertFalse(passwordService.verifyPassword("wrongpass", hashedPassword));
    }

    @Test
    void testNullPasswordHandling() {
        assertThrows(IllegalArgumentException.class, () -> {
            passwordService.hashPassword(null);
        });
        
        assertFalse(passwordService.verifyPassword(null, "hashedpwd"));
        assertFalse(passwordService.verifyPassword("plainpwd", null));
    }
}
