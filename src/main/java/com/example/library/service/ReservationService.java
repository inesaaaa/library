package com.example.library.service;

import com.example.library.exception.CustomException;
import com.example.library.model.Book;
import com.example.library.model.Reservation;
import com.example.library.model.User;
import com.example.library.repository.BookRepository;
import com.example.library.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;

    public ReservationService(ReservationRepository reservationRepository, BookRepository bookRepository) {
        this.reservationRepository = reservationRepository;
        this.bookRepository = bookRepository;
    }


    public Book reserveBook(Long bookId, User user) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new CustomException("Book not found"));
        if (book.isReserved())
            throw new CustomException("Book already reserved!");
        Reservation reservation = new Reservation();
        reservation.setBook(book);
        reservation.setUser(user);
        reservation.setReservedAt(LocalDateTime.now());
        reservationRepository.save(reservation);

        book.setReserved(true);
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
