package com.example.library.repository;

import com.example.library.model.Book;
import com.example.library.model.Reservation;
import com.example.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    void deleteByBookAndUser(Book book, User user);

    List<Reservation> findAllByUser(User user);

}