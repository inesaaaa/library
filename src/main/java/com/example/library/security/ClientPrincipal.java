package com.example.library.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClientPrincipal {
    private final Long id;
    private final String role;
}
