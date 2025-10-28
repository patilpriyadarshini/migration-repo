package com.modernized.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void constructor_ShouldCreateUser() {
        User user = new User();
        user.setSecUsrId("testuser");
        user.setSecUsrFname("John");
        user.setSecUsrLname("Doe");
        user.setSecUsrType("A");
        user.setSecUsrPwd("password123");

        assertEquals("testuser", user.getSecUsrId());
        assertEquals("John", user.getSecUsrFname());
        assertEquals("Doe", user.getSecUsrLname());
        assertEquals("A", user.getSecUsrType());
        assertEquals("password123", user.getSecUsrPwd());
    }

    @Test
    void equals_ShouldReturnTrue_WhenSameUserId() {
        User user1 = new User();
        user1.setSecUsrId("testuser");
        
        User user2 = new User();
        user2.setSecUsrId("testuser");

        assertEquals(user1, user2);
    }

    @Test
    void equals_ShouldReturnFalse_WhenDifferentUserId() {
        User user1 = new User();
        user1.setSecUsrId("testuser1");
        
        User user2 = new User();
        user2.setSecUsrId("testuser2");

        assertNotEquals(user1, user2);
    }

    @Test
    void toString_ShouldContainUserId() {
        User user = new User();
        user.setSecUsrId("testuser");
        user.setSecUsrFname("John");

        String result = user.toString();

        assertTrue(result.contains("testuser"));
    }
}
