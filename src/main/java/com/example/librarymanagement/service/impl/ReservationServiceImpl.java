package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.exception.EntityNotFoundException;
import com.example.librarymanagement.model.entity.Book;
import com.example.librarymanagement.model.entity.BookPresence;
import com.example.librarymanagement.model.entity.Library;
import com.example.librarymanagement.model.entity.Reservation;
import com.example.librarymanagement.model.entity.User;
import com.example.librarymanagement.model.enums.Availability;
import com.example.librarymanagement.repository.BookRepository;
import com.example.librarymanagement.repository.ReservationRepository;
import com.example.librarymanagement.service.BookPresenceService;
import com.example.librarymanagement.service.ReservationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;
    private final BookPresenceService bookPresenceService;

    @Override
    public List<Reservation> getReservationsByLibraryId(Long libraryId) {
        return reservationRepository.findAllByLibraryId(libraryId);
    }

    @Override
    public List<Reservation> getReservationsByUserId(Long userId) {
        return reservationRepository.findAllByUserId(userId);
    }

    @Override
    public List<Reservation> getReservationsByBookIdAndUser(Long bookId, User user) {
        return reservationRepository.findAllByBookIdAndUserId(bookId, user.getId());
    }

    @Override
    @Transactional
    public List<Reservation> doReservationBook(User user, Long libraryId, Long bookId) {
        Library library;
        List<BookPresence> bookPresenceList;
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Book"));

        if (libraryId != null) {
            bookPresenceList = bookPresenceService.getAllBookByLibraryIdAndBookId(libraryId, bookId);
            library = bookPresenceList.get(0).getLibrary();
        } else {
            bookPresenceList = bookPresenceService.getByBookId(bookId);
            library = null;
        }

        Optional<BookPresence> bookPresence = bookPresenceList.stream()
                .filter(i -> i.getAvailability().equals(Availability.AVAILABLE)).findFirst();

        if (bookPresence.isPresent()) {
            bookPresenceService.addUserToBook(user, libraryId, bookId);
        } else {
            Reservation reservation = new Reservation();
            reservation.setUser(user);
            reservation.setBook(book);
            reservation.setLibrary(library);

            book.getReservations().add(reservation);
            user.getReservations().add(reservation);

            reservationRepository.save(reservation);
        }
        return user.getReservations();
    }

    @Override
    @Transactional
    public void removeReservation(User user, Long bookId) {
        List<Reservation> reservations = getReservationsByBookIdAndUser(bookId, user);
        for (Reservation reservation : reservations) {
            reservation.getBook().getReservations().remove(reservation);
            reservation.getUser().getReservations().remove(reservation);
            deleteReservationById(reservation.getId());
        }
    }

    @Override
    public void deleteReservationById(Long id) {
        reservationRepository.findById(id)
                .ifPresent(reservation -> {
                    reservation.setBook(null);
                    reservation.setUser(null);
                    reservation.setLibrary(null);

                    reservationRepository.delete(reservation);
                });
    }
}