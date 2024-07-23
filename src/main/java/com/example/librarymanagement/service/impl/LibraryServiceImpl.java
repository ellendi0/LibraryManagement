package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.exception.EntityNotFoundException;
import com.example.librarymanagement.model.entity.Book;
import com.example.librarymanagement.model.entity.BookPresence;
import com.example.librarymanagement.model.entity.Library;
import com.example.librarymanagement.model.entity.Reservation;
import com.example.librarymanagement.model.enums.Availability;
import com.example.librarymanagement.repository.LibraryRepository;
import com.example.librarymanagement.service.BookService;
import com.example.librarymanagement.service.BookPresenceService;
import com.example.librarymanagement.service.LibraryService;
import com.example.librarymanagement.service.ReservationService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LibraryServiceImpl implements LibraryService {
    private final LibraryRepository libraryRepository;
    private final BookPresenceService bookPresenceService;
    private final BookService bookService;
    private final ReservationService reservationService;

    public LibraryServiceImpl(LibraryRepository libraryRepository,
                              BookPresenceService bookPresenceService,
                              BookService bookService,
                              ReservationService reservationService) {
        this.libraryRepository = libraryRepository;
        this.bookPresenceService = bookPresenceService;
        this.bookService = bookService;

        this.reservationService = reservationService;
    }

    @Override
    public List<Library> findAll() {
        return libraryRepository.findAll();
    }

    @Override
    public Library getLibraryById(Long id) {
        return libraryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Library"));
    }

    @Override
    public Library createLibrary(Library library) {
        return libraryRepository.save(library);
    }

    @Override
    @Transactional
    public Library addBookToLibrary(Long libraryId, Long bookId) {
        Library library = getLibraryById(libraryId);
        Book book = bookService.getBookById(bookId);
        BookPresence bookPresence = new BookPresence(book, library);

        book.getBookPresence().add(bookPresence);
        library.getBookPresence().add(bookPresence);
        bookPresenceService.createBookPresence(bookPresence);

        return libraryRepository.save(library);
    }

    @Override
    public List<BookPresence> getAllBooksByLibraryIdAndAvailability(Long libraryId, Availability availability) {
        return bookPresenceService.getAllBookByLibraryIdAndAvailability(libraryId, availability);
    }

    @Override
    public List<BookPresence> getAllBooksByLibraryId(Long libraryId) {
        return bookPresenceService.getByLibraryId(libraryId);
    }

    @Override
    public void removeBookFromLibrary(Long libraryId, Long bookPresenceId){
        Library library = getLibraryById(libraryId);
        BookPresence bookPresence = bookPresenceService.getById(bookPresenceId);

        library.getBookPresence().remove(bookPresence);

        bookPresenceService.deleteBookPresenceById(bookPresenceId);
    }

    @Override
    public Library updateLibrary(Long id, Library updatedLibrary) {
        Library library = getLibraryById(id);

        library.setName(updatedLibrary.getName());
        library.setAddress(updatedLibrary.getAddress());

        return libraryRepository.save(library);
    }

    @Override
    @Transactional
    public void deleteLibrary(Long id) {
        Optional<Library> library = libraryRepository.findById(id);

        if (library.isPresent()) {
            library.get().getBookPresence()
                    .forEach(bookPresence -> bookPresenceService.deleteBookPresenceById(bookPresence.getId()));

            List<Reservation> reservations = reservationService.getReservationsByLibraryId(id);
            if(!reservations.isEmpty()) {
                reservations.forEach(reservation -> reservationService.deleteReservationById(reservation.getId()));
            }

            libraryRepository.deleteById(id);
        }
    }
}