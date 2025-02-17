package com.example.library.model;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // nullable false means that this row cant be empty
    @Column(nullable = false)
    public String name;


    @Column(nullable = false, unique = true)
    private String username;

    private String email;

    private String password;

    // Can be ADMIN or USER
    private String role;
}
