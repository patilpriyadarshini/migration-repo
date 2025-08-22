package com.modernized.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserCreation() {
        User user = new User();
        user.setSecUsrId("testuser");
        user.setSecUsrFname("John");
        user.setSecUsrLname("Doe");
        user.setSecUsrType("ADMIN");

        assertEquals("testuser", user.getSecUsrId());
        assertEquals("John", user.getSecUsrFname());
        assertEquals("Doe", user.getSecUsrLname());
        assertEquals("ADMIN", user.getSecUsrType());
    }

    @Test
    void testUserPassword() {
        User user = new User();
        user.setSecUsrPwd("password123");

        assertEquals("password123", user.getSecUsrPwd());
    }

    @Test
    void testUserEquality() {
        User user1 = new User();
        user1.setSecUsrId("testuser");
        
        User user2 = new User();
        user2.setSecUsrId("testuser");

        assertEquals(user1.getSecUsrId(), user2.getSecUsrId());
    }
}
