package com.example.library.service;

import com.example.library.exception.CustomException;
import com.example.library.model.User;
import com.example.library.repository.UserRepository;
import com.example.library.security.JWTUtil;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

//    // Конструктор с аргументами для Spring DI
//    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JWTUtil jwtUtil) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//        this.jwtUtil = jwtUtil;
//    }

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

        return  jwtUtil.generateToken(username);
    }

}
