package com.example.library.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // nullable false means that this row cant be empty
    @Column(nullable = false)
    private String name;


    @Column(nullable = false, unique = true)
    private String username;

    private String email;


    private String password;

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    // Can be ADMIN or USER
    private String role;
}
