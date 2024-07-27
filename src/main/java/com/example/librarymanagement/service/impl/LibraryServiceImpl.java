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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LibraryServiceImpl implements LibraryService {
    private final LibraryRepository libraryRepository;
    private final BookPresenceService bookPresenceService;
    private final ReservationService reservationService;

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
    public List<BookPresence> getAllBooksByLibraryIdAndAvailability(Long libraryId, Availability availability) {
        return bookPresenceService.getAllBookByLibraryIdAndAvailability(libraryId, availability);
    }

    @Override
    public List<BookPresence> getAllBooksByLibraryId(Long libraryId) {
        return bookPresenceService.getByLibraryId(libraryId);
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
        libraryRepository.findById(id).ifPresent(library -> {
            library.getBookPresence()
                    .forEach(bookPresence -> bookPresenceService.deleteBookPresenceByIdAndLibraryId(bookPresence.getId(), id));

            List<Reservation> reservations = reservationService.getReservationsByLibraryId(id);

            if(!reservations.isEmpty()) {
                reservations.forEach(reservation -> reservationService.deleteReservationById(reservation.getId()));
            }

            libraryRepository.deleteById(id);
        });
    }
}