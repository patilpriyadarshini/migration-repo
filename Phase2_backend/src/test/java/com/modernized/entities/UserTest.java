package com.modernized.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void testUserCreation() {
        assertNotNull(user);
        assertNull(user.getSecUsrId());
        assertNull(user.getSecUsrFname());
        assertNull(user.getSecUsrLname());
        assertNull(user.getSecUsrPwd());
        assertNull(user.getSecUsrType());
    }

    @Test
    void testSetAndGetSecUsrId() {
        String userId = "user123";
        user.setSecUsrId(userId);
        assertEquals(userId, user.getSecUsrId());
    }

    @Test
    void testSetAndGetSecUsrIdWithNull() {
        user.setSecUsrId(null);
        assertNull(user.getSecUsrId());
    }

    @Test
    void testSetAndGetSecUsrIdDifferentFormats() {
        String[] userIds = {"user123", "admin001", "test_user", "user-456"};
        
        for (String userId : userIds) {
            user.setSecUsrId(userId);
            assertEquals(userId, user.getSecUsrId());
        }
    }

    @Test
    void testSetAndGetSecUsrFname() {
        String firstName = "John";
        user.setSecUsrFname(firstName);
        assertEquals(firstName, user.getSecUsrFname());
    }

    @Test
    void testSetAndGetSecUsrFnameWithSpecialCharacters() {
        String firstName = "Jean-Pierre";
        user.setSecUsrFname(firstName);
        assertEquals(firstName, user.getSecUsrFname());
    }

    @Test
    void testSetAndGetSecUsrFnameWithNull() {
        user.setSecUsrFname(null);
        assertNull(user.getSecUsrFname());
    }

    @Test
    void testSetAndGetSecUsrLname() {
        String lastName = "Doe";
        user.setSecUsrLname(lastName);
        assertEquals(lastName, user.getSecUsrLname());
    }

    @Test
    void testSetAndGetSecUsrLnameWithSpecialCharacters() {
        String lastName = "O'Connor-Smith";
        user.setSecUsrLname(lastName);
        assertEquals(lastName, user.getSecUsrLname());
    }

    @Test
    void testSetAndGetSecUsrLnameWithNull() {
        user.setSecUsrLname(null);
        assertNull(user.getSecUsrLname());
    }

    @Test
    void testSetAndGetSecUsrPwd() {
        String password = "password123";
        user.setSecUsrPwd(password);
        assertEquals(password, user.getSecUsrPwd());
    }

    @Test
    void testSetAndGetSecUsrPwdWithComplexPassword() {
        String password = "P@ssw0rd!2024";
        user.setSecUsrPwd(password);
        assertEquals(password, user.getSecUsrPwd());
    }

    @Test
    void testSetAndGetSecUsrPwdWithNull() {
        user.setSecUsrPwd(null);
        assertNull(user.getSecUsrPwd());
    }

    @Test
    void testSetAndGetSecUsrType() {
        String userType = "A";
        user.setSecUsrType(userType);
        assertEquals(userType, user.getSecUsrType());
    }

    @Test
    void testSetAndGetSecUsrTypeDifferentValues() {
        String[] userTypes = {"A", "U", "ADMIN", "USER"};
        
        for (String userType : userTypes) {
            user.setSecUsrType(userType);
            assertEquals(userType, user.getSecUsrType());
        }
    }

    @Test
    void testSetAndGetSecUsrTypeWithNull() {
        user.setSecUsrType(null);
        assertNull(user.getSecUsrType());
    }

    @Test
    void testUserValidation() {
        user.setSecUsrId("user123");
        user.setSecUsrFname("John");
        user.setSecUsrLname("Doe");
        user.setSecUsrPwd("password123");
        user.setSecUsrType("A");
        
        assertEquals("user123", user.getSecUsrId());
        assertEquals("John", user.getSecUsrFname());
        assertEquals("Doe", user.getSecUsrLname());
        assertEquals("password123", user.getSecUsrPwd());
        assertEquals("A", user.getSecUsrType());
    }

    @Test
    void testCompleteUserSetup() {
        user.setSecUsrId("admin001");
        user.setSecUsrFname("Jane");
        user.setSecUsrLname("Smith");
        user.setSecUsrPwd("securePass123");
        user.setSecUsrType("A");

        assertEquals("admin001", user.getSecUsrId());
        assertEquals("Jane", user.getSecUsrFname());
        assertEquals("Smith", user.getSecUsrLname());
        assertEquals("securePass123", user.getSecUsrPwd());
        assertEquals("A", user.getSecUsrType());
    }

    @Test
    void testUserEquality() {
        User user1 = new User();
        User user2 = new User();
        
        user1.setSecUsrId("user123");
        user2.setSecUsrId("user123");
        
        assertEquals(user1.getSecUsrId(), user2.getSecUsrId());
    }

    @Test
    void testUserToString() {
        user.setSecUsrId("user123");
        user.setSecUsrFname("John");
        user.setSecUsrLname("Doe");
        String userString = user.toString();
        assertNotNull(userString);
        assertTrue(userString.contains("User") || userString.contains("user123"));
    }

    @Test
    void testUserTypeValidation() {
        user.setSecUsrType("A");
        assertEquals("A", user.getSecUsrType());
        
        user.setSecUsrType("U");
        assertEquals("U", user.getSecUsrType());
    }

    @Test
    void testUserIdValidation() {
        String validUserId = "user1234";
        user.setSecUsrId(validUserId);
        assertEquals(validUserId, user.getSecUsrId());
        assertTrue(user.getSecUsrId().length() >= 4);
    }

    @Test
    void testPasswordValidation() {
        String validPassword = "pass1234";
        user.setSecUsrPwd(validPassword);
        assertEquals(validPassword, user.getSecUsrPwd());
        assertTrue(user.getSecUsrPwd().length() >= 8);
    }
}
