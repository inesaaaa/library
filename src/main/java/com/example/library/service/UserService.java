package com.example.library.service;

import com.example.library.exception.CustomException;
import com.example.library.model.User;
import com.example.library.repository.UserRepository;
import com.example.library.security.JWTUtil;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JWTUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }


    public User registerUser(User user) {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            throw new CustomException("Username already exists!");
        }
        // Шифруем пароль перед сохранением.
        // Пароли никогда не должны храниться в базе данных в открытом виде.
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("Invalid username or password"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException("Invalid username or password");
        }

        return  jwtUtil.generateToken(username, user.getRole());
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("User not found"));
    }


    public void deleteUser(String username, String password, String token) {
        String role = jwtUtil.getRoleFromToken(token);

        if ("admin".equalsIgnoreCase(role)) {
            // Admins can delete any user without password
            Optional<User> optionalUser = userRepository.findByUsername(username);

            if (optionalUser.isPresent()) {
                userRepository.delete(optionalUser.get());
                System.out.println("Admin deleted user: " + username);
            } else {
                throw new IllegalArgumentException("User not found: " + username);
            }
        } else if ("user".equalsIgnoreCase(role)) {
            // Users can delete themselves only if the password matches
            Optional<User> optionalUser = userRepository.findByUsername(username);

            if (optionalUser.isEmpty()) {
                throw new IllegalArgumentException("User not found: " + username);
            }

            User user = optionalUser.get();

            // Validate password
            if (password == null || !passwordEncoder.matches(password, user.getPassword())) {
                throw new SecurityException("Incorrect password provided for user: " + username);
            }

            // Proceed with deletion
            userRepository.delete(user);
            System.out.println("User deleted their own account: " + username);
        } else {
            // Role is neither admin nor user (unauthorized)
            throw new SecurityException("Unauthorized delete attempt by role: " + role);
        }
    }
}
