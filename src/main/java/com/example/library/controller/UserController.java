package com.example.library.controller;

import com.example.library.dto.LoginRequestDTO;
import com.example.library.model.User;
import com.example.library.security.JWTUtil;
import com.example.library.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JWTUtil jwtUtil;

    public UserController(UserService userService, JWTUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registeredUser(@Valid @RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequestDTO dto) {
        String token = userService.login(dto.getUsername(), dto.getPassword());
        return ResponseEntity.ok(token);
    }

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String token) {
        String username = jwtUtil.validateToken(token);
        System.out.printf("Username : " + username);
        User user = userService.findByUsername(username);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteUser(
            @PathVariable String username,
            @RequestBody(required = false) Map<String, String> requestBody,
            @RequestHeader("Authorization") String token) {
        try {
            String password = requestBody != null ? requestBody.get("password") : null;
            userService.deleteUser(username, password, token.substring(7)); // Removes "Bearer " prefix
            return ResponseEntity.ok("User deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }
}