package com.example.library.controller;

import com.example.library.model.Book;
import com.example.library.model.Reservation;
import com.example.library.model.User;
import com.example.library.service.BookService;
import com.example.library.service.ReservationService;
import com.example.library.service.UserService;
import com.example.library.security.JWTUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final BookService bookService;
    private final UserService userService;
    private final JWTUtil jwtUtil;

    public ReservationController(ReservationService reservationService,
                                 BookService bookService,
                                 UserService userService,
                                 JWTUtil jwtUtil) {
        this.reservationService = reservationService;
        this.bookService = bookService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/{bookId}/reserve")
    public ResponseEntity<?> reserveBook(@PathVariable Long bookId, @RequestHeader("Authorization") String token) {
        String username = jwtUtil.validateToken(token);
        User user = userService.findByUsername(username);

        Book book = reservationService.reserveBook(bookId, user);
        return ResponseEntity.ok(book);
    }

    @PostMapping("/{bookId}/unreserve")
    public ResponseEntity<?> unreserveBook(@PathVariable Long bookId, @RequestHeader("Authorization") String token) {
        String username = jwtUtil.validateToken(token);
        User user = userService.findByUsername(username);

        Book book = reservationService.unreserveBook(bookId, user);
        return ResponseEntity.ok(book);
    }

    @GetMapping("/history")
    public ResponseEntity<?> getUserReservationHistory(@RequestHeader("Authorization") String token) {
        String username = jwtUtil.validateToken(token);
        User user = userService.findByUsername(username);

        List<Reservation> reservations = reservationService.getUserReservations(user);
        return ResponseEntity.ok(reservations);
    }

//    @PostMapping("/{bookId}/reserve")
//    public ResponseEntity<?> reserveBook(@PathVariable Long bookId, @RequestHeader("Authorization") String token) {
//        Book book = new Book();
//        User user = new User();
//        if (jwtUtil.validateToken(token)) {
//            String username = jwtUtil.getUsernameFromToken(token);
//            user = userService.findByUsername(username);
//            book = reservationService.reserveBook(bookId, user);
//        }
//
//        return ResponseEntity.ok(book);
//    }
//
//    @PostMapping("/{bookId}/unreserve")
//    public ResponseEntity<?> unreserveBook(@PathVariable Long bookId, @RequestHeader("Authorization") String token) {
//        User user = new User();
//        Book book = new Book();
//        if (jwtUtil.validateToken(token)) {
//            String username = jwtUtil.getUsernameFromToken(token);
//            user = userService.findByUsername(username);
//            book = reservationService.unreserveBook(bookId, user);
//        }
//        return ResponseEntity.ok(book);
//    }
//
//    @GetMapping("/history")
//    public ResponseEntity<?> getUserReservationHistory(@RequestHeader("Authorization") String token) {
//        User user = new User();
//        List<Reservation> reservations = null;
//        if (jwtUtil.validateToken(token)) {
//            String username = jwtUtil.getRoleFromToken(token);
//            user = userService.findByUsername(username);
//            reservations = reservationService.getUserReservations(user);
//        }
//        return ResponseEntity.ok(reservations);
//    }

    @GetMapping("/admin/history")
    public ResponseEntity<?> getAllReservations(@RequestHeader("Authorization") String token) {
        String role = jwtUtil.getRoleFromToken(token);
        if (!"ADMIN".equals(role)) {
            throw new RuntimeException("Access denied: Only admins can access this endpoint!");
        }

        List<Reservation> allReservations = reservationService.getAllReservations();
        return ResponseEntity.ok(allReservations);
    }
}