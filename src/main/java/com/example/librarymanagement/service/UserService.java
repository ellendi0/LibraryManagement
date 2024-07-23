package com.example.librarymanagement.service;

import com.example.librarymanagement.model.entity.Journal;
import com.example.librarymanagement.model.entity.Reservation;
import com.example.librarymanagement.model.entity.User;

import java.util.List;

public interface UserService {
    User getUserByPhoneNumberOrEmail(String email, String phoneNumber);
    User getUserById(Long id);
    User createUser(User user);
    User updateUser(Long id, User updatedUser);
    List<User> findAll();
    List<Reservation> findReservationsByUser(Long userId);
    List<Journal> findJournalsByUser(Long userId);
    List<Journal> borrowBookFromLibrary(Long userId, Long libraryId, Long bookId);
    List<Reservation> reserveBookInLibrary(Long userId, Long libraryId, Long bookId);
    void cancelReservationInLibrary(Long userId, Long bookId);
    List<Journal> returnBookToLibrary(Long userId, Long libraryId, Long bookId);
    void deleteUser(Long id);
}