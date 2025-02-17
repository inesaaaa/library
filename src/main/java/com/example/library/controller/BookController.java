package com.example.library.controller;

import com.example.library.dto.BookRequestDTO;
import com.example.library.dto.DeleteBookRequestDTO;
import com.example.library.exception.CustomException;
import com.example.library.model.Book;
import com.example.library.security.JWTUtil;
import com.example.library.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;
    private final JWTUtil jwtUtil;

    public BookController(BookService bookService, JWTUtil jwtUtil) {
        this.bookService = bookService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PostMapping("/findByNameAndAuthor")
    public ResponseEntity<Book> getBookByNameAndAuthor(@RequestBody BookRequestDTO request) {
        try {
            Book book = bookService.getBookByTitleAndAuthor(request.getTitle(), request.getAuthor());
            return ResponseEntity.ok(book);
        } catch (CustomException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @DeleteMapping("/deleteByTitleAndAuthor")
    public ResponseEntity<String> deleteBookByTitleAndAuthor(@RequestBody DeleteBookRequestDTO request) {
        try {
            bookService.deleteBookByTitleAndAuthor(request.getTitle(), request.getAuthor());
            return ResponseEntity.ok("Book deleted successfully");
        } catch (CustomException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }


    @PostMapping("/add")
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        return ResponseEntity.ok(bookService.addBook(book));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        String role = jwtUtil.getRoleFromToken(token);

        if (!"ADMIN".equals(role)) {
            throw new CustomException("Access denied: Only admins can delete books!");
        }

        bookService.deleteBook(id);
        return ResponseEntity.ok("Book deleted successfully");
    }

}
