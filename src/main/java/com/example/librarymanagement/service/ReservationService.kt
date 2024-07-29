package com.example.librarymanagement.service;

import com.example.librarymanagement.model.entity.Reservation;
import com.example.librarymanagement.model.entity.User;

import java.util.List;

public interface ReservationService {
    List<Reservation> getReservationsByLibraryId(Long libraryId);
    List<Reservation> getReservationsByUserId(Long userId);
    List<Reservation> getReservationsByBookIdAndUser(Long bookId, User user);
    List<Reservation> doReservationBook(User user, Long libraryId, Long bookId);
    void removeReservation(User user, Long bookId);
    void deleteReservationById(Long id);
}