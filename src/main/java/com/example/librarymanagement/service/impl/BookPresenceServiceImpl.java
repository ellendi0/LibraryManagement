package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.exception.BookNotAvailableException;
import com.example.librarymanagement.exception.EntityNotFoundException;
import com.example.librarymanagement.model.entity.Book;
import com.example.librarymanagement.model.entity.BookPresence;
import com.example.librarymanagement.model.entity.Journal;
import com.example.librarymanagement.model.entity.Library;
import com.example.librarymanagement.model.entity.Reservation;
import com.example.librarymanagement.model.entity.User;
import com.example.librarymanagement.model.enums.Availability;
import com.example.librarymanagement.repository.BookPresenceRepository;
import com.example.librarymanagement.repository.BookRepository;
import com.example.librarymanagement.repository.LibraryRepository;
import com.example.librarymanagement.repository.ReservationRepository;
import com.example.librarymanagement.service.BookPresenceService;
import com.example.librarymanagement.service.JournalService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookPresenceServiceImpl implements BookPresenceService {
    private final BookPresenceRepository bookPresenceRepository;
    private final BookRepository bookRepository;
    private final LibraryRepository libraryRepository;
    private final JournalService journalService;
    private final ReservationRepository reservationRepository;

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
                .orElseThrow(() -> new BookNotAvailableException(libraryId, bookId));

        Journal journal = new Journal();
        journal.setBookPresence(bookPresence);
        journal.setDateOfBorrowing(LocalDate.now());
        journal.setUser(user);

        bookPresence.getJournals().add(journal);

        journalService.createJournal(journal);

        bookPresence.setUser(user);
        bookPresence.setAvailability(Availability.UNAVAILABLE);

        return bookPresenceRepository.save(bookPresence);
    }

    @Override
    @Transactional
    public BookPresence addBookToLibrary(Long libraryId, Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Book"));
        Library library = libraryRepository.findById(libraryId).orElseThrow(() -> new EntityNotFoundException("Library"));

        BookPresence bookPresence = new BookPresence();
        bookPresence.setBook(book);
        bookPresence.setLibrary(library);

        return createBookPresence(bookPresence);
    }

    @Override
    @Transactional
    public BookPresence removeUserFromBook(User user, Long libraryId, Long bookId) {
        BookPresence bookPresence = bookPresenceRepository.findAllByLibraryIdAndBookIdAndUser(libraryId, bookId, user)
                .orElseThrow(() -> new EntityNotFoundException("BookPresence"));

        Journal journal = journalService.findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(bookPresence.getId(), user.getId());

        journal.setDateOfReturning(LocalDate.now());

        journalService.updateJournal(journal.getId(), journal);

        Reservation reservation = reservationRepository.findFirstByBookIdAndLibraryIdOrLibraryIsNull(bookId, libraryId);
        if(reservation != null){
            bookPresence.setUser(reservation.getUser());
        }else {
            bookPresence.setAvailability(Availability.AVAILABLE);
            bookPresence.setUser(null);
        }

        return createBookPresence(bookPresence);
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
        return bookPresenceRepository.findAllByLibraryIdAndAvailability(libraryId, availability);
    }

    @Override
    public List<BookPresence> findAllByLibraryIdAndBookIdAndAvailability(Long libraryId, Long bookId, Availability availability) {
        return bookPresenceRepository.findAllByLibraryIdAndBookIdAndAvailability(libraryId, bookId, availability);
    }

    @Override
    @Transactional
    public void deleteBookPresenceByIdAndLibraryId(Long libraryId, Long bookPresenceId) {
        bookPresenceRepository.deleteBookPresenceByIdAndLibraryId(bookPresenceId, libraryId);
    }

    @Override
    public void deleteBookPresenceById(Long id) {
        bookPresenceRepository.deleteById(id);
    }
}