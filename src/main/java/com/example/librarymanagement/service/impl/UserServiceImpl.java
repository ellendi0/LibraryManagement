package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.exception.DuplicateKeyException;
import com.example.librarymanagement.exception.EntityNotFoundException;
import com.example.librarymanagement.model.entity.BookPresence;
import com.example.librarymanagement.model.entity.Journal;
import com.example.librarymanagement.model.entity.Reservation;
import com.example.librarymanagement.model.entity.User;
import com.example.librarymanagement.repository.BookPresenceRepository;
import com.example.librarymanagement.repository.UserRepository;
import com.example.librarymanagement.service.BookPresenceService;
import com.example.librarymanagement.service.ReservationService;
import com.example.librarymanagement.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BookPresenceService bookPresenceService;
    private final BookPresenceRepository bookPresenceRepository;
    private final ReservationService reservationService;
    private final JournalServiceImpl journalServiceImpl;

    @Override
    public User getUserByPhoneNumberOrEmail(String email, String phoneNumber) {
        return userRepository.findByEmailOrPhoneNumber(email, phoneNumber).orElseThrow(
                () -> new EntityNotFoundException("User"));
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User"));
    }

    @Override
    public User createUser(User user) {
        if(userRepository.existsByEmail(user.getEmail())){
            throw new DuplicateKeyException("User", "email");
        }
        if(userRepository.existsByPhoneNumber(user.getPhoneNumber())){
            throw new DuplicateKeyException("User", "phoneNumber");
        }

        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User updatedUser) {
        User user = getUserById(id);

        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(updatedUser.getPassword());
        user.setPhoneNumber(updatedUser.getPhoneNumber());

        return userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<Reservation> findReservationsByUser(Long id) {
        getUserById(id);
        return reservationService.getReservationsByUserId(id);
    }

    @Override
    public List<Journal> findJournalsByUser(Long userId) {
        getUserById(userId);
        return journalServiceImpl.getJournalByUserId(userId);
    }

    @Override
    public List<Journal> borrowBookFromLibrary(Long userId, Long libraryId, Long bookId) {
        return bookPresenceService.addUserToBook(getUserById(userId), libraryId, bookId).getUser().getJournals();
    }

    @Override
    public List<Reservation> reserveBookInLibrary(Long userId, Long libraryId, Long bookId) {
        return reservationService.doReservationBook(getUserById(userId), libraryId, bookId);
    }

    @Override
    public void cancelReservationInLibrary(Long userId, Long reservationId) {
        reservationService.removeReservation(getUserById(userId), reservationId);
    }

    @Override
    public List<Journal> returnBookToLibrary(Long userId, Long libraryId, Long bookId) {
        return bookPresenceService.removeUserFromBook(getUserById(userId), libraryId, bookId).getJournals();
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.findById(id).ifPresent(user -> {

            List<BookPresence> bookPresence = bookPresenceRepository.findAllByUserId(id);

            if (!CollectionUtils.isEmpty(bookPresence)) {
                bookPresence.forEach(b -> b.setUser(null));
            }

            user.getJournals().forEach(journal -> journalServiceImpl.deleteJournal(journal.getId()));
            user.getReservations().forEach(reservation -> reservationService.deleteReservationById(reservation.getId()));

            userRepository.delete(user);
        });
    }
}