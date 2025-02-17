package com.example.library.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.DecodingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtil {
    @Value("${jwt.secret}") // Inject the 'jwt.secret' property from application.properties
    private String secretKey;

    // Generate a JWT token for a user
//    public String generateToken(String username) {
//        return Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
//                .signWith(SignatureAlgorithm.HS256, secretKey)
//                .compact();
//
//    }

    public String generateToken(String username, String role) {
        long now = System.currentTimeMillis();
        long expiration = now + 1000 * 60 * 60 * 10; // 10 hours

        System.out.println("Issued at: " + new Date(now));
        System.out.println("Expiration: " + new Date(expiration));

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(expiration))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        try {
            // Remove "Bearer " if present
            if (token.startsWith("Bearer ")) {
                token = token.substring(7).trim();
            }

            // Parse the token to extract claims
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            System.out.println("Error while extracting username: " + e.getMessage());
            return null; // Return null or throw a custom exception (if needed)
        }
    }


    // Validate the token and extract the username
//    public  String validateToken(String token) {
//        return Jwts.parser()
//                .setSigningKey(secretKey)
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();
//    }

    public String validateToken(String token) {
        try {
            // Remove "Bearer " if it's included, then trim any additional spaces
            if (token.startsWith("Bearer ")) {
                token = token.substring(7).trim();
            }

            // Parse the token
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            return getUsernameFromToken(token);

        } catch (MalformedJwtException ex) {
            System.out.println("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            System.out.println("JWT token is expired");
        } catch (UnsupportedJwtException ex) {
            System.out.println("JWT token is unsupported");
        } catch (IllegalArgumentException ex) {
            System.out.println("JWT claims string is empty");
        } catch (DecodingException ex) {
            System.out.println("Token contains invalid characters");
        }
        return "Invalid Token";
    }

//    public boolean validateToken(String token) {
//        try {
//            // Remove "Bearer " if it's included, then trim any additional spaces
//            if (token.startsWith("Bearer ")) {
//                token = token.substring(7).trim();
//            }
//
//            // Parse the token
//            Jwts.parser()
//                    .setSigningKey(secretKey)
//                    .parseClaimsJws(token);
//
//            return true; // Valid token
//        } catch (MalformedJwtException ex) {
//            System.out.println("Invalid JWT token");
//        } catch (ExpiredJwtException ex) {
//            System.out.println("JWT token is expired");
//        } catch (UnsupportedJwtException ex) {
//            System.out.println("JWT token is unsupported");
//        } catch (IllegalArgumentException ex) {
//            System.out.println("JWT claims string is empty");
//        } catch (DecodingException ex) {
//            System.out.println("Token contains invalid characters");
//        }
//        return false; // Invalid token
//    }

    // Utility method to check if the token is expired
    public boolean isTokenExpired(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    public String getRoleFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }
}
