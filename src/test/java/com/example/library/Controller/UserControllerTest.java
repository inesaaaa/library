package com.example.library.Controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*; // Correct import for post and get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*; // Correct import for status and jsonPath


@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String token;

    @BeforeAll
    void setUp() throws Exception {
        // Register a new user
        String userJson = "{ \"name\": \"Test User\", \"username\": \"testuser\", \"password\": \"password\", \"email\": \"test@example.com\", \"role\": \"USER\" }";

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk());

        String loginJson = "{ \"username\": \"testuser\", \"password\": \"password\" }";
        token = mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        System.out.println("Retrieved Token: " + token);

    }


    @Test
    @Order(1)
    void testGetUserInfo() throws Exception {
        mockMvc.perform(get("/api/users/info")
                        .header("Authorization", "Bearer " + token)) // Use the generated token
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    void testLoginUser() throws Exception {
        String loginJson = "{ \"username\": \"testuser\", \"password\": \"password\" }";
        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    void testDeleteUser() throws Exception {
        String requestBody = "{ \"password\": \"password\" }";

        mockMvc.perform(delete("/api/users/testuser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", "Bearer " + token)) // Use the same token
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));
    }
}