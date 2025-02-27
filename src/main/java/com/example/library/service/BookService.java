package com.example.library.service;

import com.example.library.exception.CustomException;
import com.example.library.model.Book;
import com.example.library.model.Reservation;
import com.example.library.model.User;
import com.example.library.repository.BookRepository;
import com.example.library.repository.ReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final ReservationRepository reservationRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new CustomException("Book not found"));
    }

    public Book getBookByTitleAndAuthor(String title, String author) {
        return bookRepository.findByTitleAndAuthor(title, author)
                .orElseThrow(() -> new CustomException("Book not found with provided name and author"));
    }

    public void deleteBookByTitleAndAuthor(String title, String author) {
        Book book = bookRepository.findByTitleAndAuthor(title, author)
                .orElseThrow(() -> new CustomException("Book not found with provided name and author"));

        bookRepository.delete(book);
    }

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public Book reserveBook(Long bookId, User user) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new CustomException("Book not found"));
        book.setReserved(true);

        if (book.isReserved())
            throw new CustomException("Book already reserved!");

        book.setReserved(true);

        Reservation reservation = new Reservation();
        reservation.setBook(book);
        reservation.setUser(user);
        reservation.setReservedAt(LocalDateTime.now());

        reservationRepository.save(reservation);

        return bookRepository.save(book);
    }

    public Book unreserveBook(Long bookId, User user) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new CustomException("Book not found"));
        if (!book.isReserved())
            throw new CustomException("Book is not reserved!");
        reservationRepository.deleteByBookAndUser(book, user);
        book.setReserved(false);
        return bookRepository.save(book);
    }

    public List<Reservation> getUserReservations(User user) {
        return reservationRepository.findAllByUser(user);
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

}
