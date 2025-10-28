package com.modernized.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserCreation() {
        User user = new User("TESTUSER", "John", "Doe", "hashedpwd", "A");
        
        assertEquals("TESTUSER", user.getSecUsrId());
        assertEquals("John", user.getSecUsrFname());
        assertEquals("Doe", user.getSecUsrLname());
        assertEquals("hashedpwd", user.getSecUsrPwd());
        assertEquals("A", user.getSecUsrType());
    }

    @Test
    void testUserEquality() {
        User user1 = new User("TESTUSER", "John", "Doe", "hashedpwd", "A");
        User user2 = new User("TESTUSER", "Jane", "Smith", "otherpwd", "R");
        User user3 = new User("DIFFUSER", "John", "Doe", "hashedpwd", "A");
        
        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
        assertEquals(user1.hashCode(), user2.hashCode());
    }
}
