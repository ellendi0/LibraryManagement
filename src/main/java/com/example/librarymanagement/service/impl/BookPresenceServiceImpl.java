package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.exception.EntityNotFoundException;
import com.example.librarymanagement.model.entity.Journal;
import com.example.librarymanagement.model.entity.Reservation;
import com.example.librarymanagement.model.enums.Availability;
import com.example.librarymanagement.model.entity.BookPresence;
import com.example.librarymanagement.model.entity.User;
import com.example.librarymanagement.repository.BookPresenceRepository;
import com.example.librarymanagement.repository.ReservationRepository;
import com.example.librarymanagement.service.BookPresenceService;
import com.example.librarymanagement.service.JournalService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookPresenceServiceImpl implements BookPresenceService {
    private final BookPresenceRepository bookPresenceRepository;
    private final JournalService journalService;
    private final ReservationRepository reservationRepository;

    public BookPresenceServiceImpl(BookPresenceRepository bookPresenceRepository,
                                   JournalService journalService,
                                   ReservationRepository reservationRepository) {
        this.bookPresenceRepository = bookPresenceRepository;
        this.journalService = journalService;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public BookPresence createBookPresence(BookPresence bookPresence) {
        return bookPresenceRepository.save(bookPresence);
    }

    @Override
    @Transactional
    public BookPresence addUserToBook(User user, Long libraryId, Long bookId) {
        BookPresence bookPresence = findAllByLibraryIdAndBookIdAndAvailability(libraryId, bookId, Availability.AVAILABLE)
                .stream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Unavailable books"));

        Journal journal = new Journal(user, bookPresence);
        bookPresence.getJournals().add(journal);
        user.getJournals().add(journal);

        journalService.createJournal(journal);

        bookPresence.setUser(user);
        bookPresence.setAvailability(Availability.NOT_AVAILABLE);

        return bookPresenceRepository.save(bookPresence);
    }

    @Override
    @Transactional
    public BookPresence removeUserFromBook(User user, Long libraryId, Long bookId) {
        BookPresence bookPresence = getAllBookByLibraryIdAndBookId(libraryId, bookId).stream()
                .filter(i -> i.getUser().equals(user))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Book"));

        Journal journal = journalService.getJournalByBookPresenceIdAndUserId(bookPresence.getId(), user.getId());
        journal.setDateOfReturning(LocalDate.now());

        journalService.createJournal(journal);

        List<Reservation> reservations = reservationRepository.findAllByBookIdAndUserId(bookId, user.getId());
        if(!reservations.isEmpty()){
            bookPresence.setUser(reservations.get(0).getUser());
        }else {
            bookPresence.setAvailability(Availability.AVAILABLE);
            bookPresence.setUser(null);
        }

        return bookPresenceRepository.save(bookPresence);
    }

    @Override
    public BookPresence getById(Long id) {
        return bookPresenceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book"));
    }

    @Override
    public List<BookPresence> getByBookId(Long bookId) {
        return bookPresenceRepository.findAllByBookId(bookId);
    }

    @Override
    public List<BookPresence> getByLibraryId(Long libraryId) {
        return bookPresenceRepository.findAllByLibraryId(libraryId);
    }

    @Override
    public List<BookPresence> getAllBookByLibraryIdAndBookId(Long libraryId, Long bookId) {
        return bookPresenceRepository.findAllByLibraryIdAndBookId(libraryId, bookId);
    }

    @Override
    public List<BookPresence> getAllBookByLibraryIdAndAvailability(Long libraryId, Availability availability) {
        return getByLibraryId(libraryId).stream()
                .filter(i -> i.getAvailability().equals(availability))
                .toList();
    }

    @Override
    public List<BookPresence> findAllByLibraryIdAndBookIdAndAvailability(Long libraryId, Long bookId, Availability availability) {
        return bookPresenceRepository.findAllByLibraryIdAndBookIdAndAvailability(libraryId, bookId, availability);
    }

    @Override
    public void deleteBookPresenceById(Long id) {
        Optional<BookPresence> bookPresence = bookPresenceRepository.findById(id);

        if(bookPresence.isPresent()){
            bookPresence.get().setLibrary(null);
            bookPresence.get().setBook(null);

            List<Journal> journals = journalService.getJournalByBookPresenceId(id);
            if(!journals.isEmpty()){
                journals.forEach(journal -> journal.setBookPresence(null));
            }

            bookPresenceRepository.delete(bookPresence.get());
        }
    }
}