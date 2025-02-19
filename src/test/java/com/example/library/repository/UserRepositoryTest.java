package com.example.library.repository;

import com.example.library.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        User user = new User();
        user.setName("Test User"); // Ensure 'name' is set
        user.setUsername("existinguser");
        user.setPassword("password123");
        user.setEmail("testuser@example.com");
        user.setRole("USER");
        userRepository.save(user);
    }

    @Test
    void testSaveUser() {
        User user = new User();
        user.setName("Unit Test User");
        user.setUsername("unittestuser");
        user.setPassword("password123");
        user.setEmail("unituser@example.com");
        user.setRole("USER");

        User savedUser = userRepository.save(user);

        assertNotNull(savedUser.getId(), "ID should not be null after save.");
        assertEquals("unittestuser", savedUser.getUsername(), "The user should be saved with the correct username.");
        assertEquals("Unit Test User", savedUser.getName(), "Name should be saved correctly.");
    }

    @Test
    public void testFindByUsername() {
        User user = new User();
        user.setName("Find By Username Test");
        user.setUsername("findbyusernametest");
        user.setPassword("password123");
        user.setEmail("findbyusernametest@example.com");
        user.setRole("USER");
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByUsername("findbyusernametest");
        assertTrue(foundUser.isPresent(), "User with 'findbyusernametest' username should be found.");
    }

    @Test
    public void testDeleteUser() {
        User user = new User();
        user.setName("Delete User Test");
        user.setUsername("deleteusertest");
        user.setPassword("password123");
        user.setEmail("deleteusertest@example.com");
        user.setRole("USER");
        User savedUser = userRepository.save(user);

        userRepository.deleteById(savedUser.getId());

        Optional<User> deletedUser = userRepository.findByUsername("deleteusertest");
        assertFalse(deletedUser.isPresent(), "User with 'deleteusertest' username should not present.");
    }

    @Test
    void testFindNonExistingUser() {
        Optional<User> user = userRepository.findByUsername("nonexistentuser");
        assertFalse(user.isPresent(), "The user 'nonexistentuser' should not be found.");
    }

}
