package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.data.TestDataFactory;
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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookPresenceImplTest {
    @InjectMocks
    private BookPresenceServiceImpl bookPresenceServiceImpl;

    @Mock
    private BookPresenceRepository bookPresenceRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private LibraryRepository libraryRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private JournalServiceImpl journalService;

    private static BookPresence bookPresence;
    private static Library library;
    private static Book book;
    private static User user;
    private static Journal journal;
    private static Reservation reservation;

    @BeforeAll
    public static void init() {
        TestDataFactory.TestDataRel data = TestDataFactory.createTestDataRel();
        bookPresence = data.bookPresence;
        library = data.library;
        book = data.book;
        user = data.user;
        journal = data.journal;
        reservation = data.reservation;
    }

    @Test
    public void createBookPresence() {
        when(bookPresenceRepository.save(bookPresence)).thenReturn(bookPresence);

        assertEquals(bookPresence, bookPresenceServiceImpl.createBookPresence(bookPresence));
        verify(bookPresenceRepository, times(1)).save(bookPresence);
    }

    @Test
    public void addUserToBook() {
        when(bookPresenceRepository
                .findAllByLibraryIdAndBookIdAndAvailability(library.getId(), book.getId(), Availability.AVAILABLE))
                .thenReturn(List.of(bookPresence));
        when(bookPresenceRepository.save(any(BookPresence.class))).thenReturn(bookPresence);
        when(journalService.createJournal(any(Journal.class))).thenReturn(journal);

        assertEquals(bookPresence, bookPresenceServiceImpl.addUserToBook(user, library.getId(), book.getId()));
        verify(bookPresenceRepository, times(1))
                .findAllByLibraryIdAndBookIdAndAvailability(1L, 1L, Availability.AVAILABLE);
        verify(bookPresenceRepository, times(1)).save(any(BookPresence.class));
        verify(journalService, times(1)).createJournal(any(Journal.class));
    }

    @Test
    public void addUserToBook_BookNotAvailable() {
        when(bookPresenceRepository
                .findAllByLibraryIdAndBookIdAndAvailability(library.getId(), book.getId(), Availability.AVAILABLE))
                .thenReturn(List.of());

        assertThrows(BookNotAvailableException.class, () -> {
            bookPresenceServiceImpl.addUserToBook(user, library.getId(), book.getId());
        });

        verify(bookPresenceRepository, times(1))
                .findAllByLibraryIdAndBookIdAndAvailability(1L, 1L, Availability.AVAILABLE);
        verify(bookPresenceRepository, never()).save(any(BookPresence.class));
        verify(journalService, never()).createJournal(any(Journal.class));
    }

    @Test
    public void removeUserFromBook() {
        when(bookPresenceRepository.findAllByLibraryIdAndBookIdAndUser(library.getId(), book.getId(), user))
                .thenReturn(Optional.of(bookPresence));
        when(journalService.findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(bookPresence.getId(), user.getId()))
                .thenReturn(journal);
        when(journalService.updateJournal(journal.getId(), journal)).thenReturn(journal);
        when(reservationRepository.findFirstByBookIdAndLibraryIdOrLibraryIsNull(book.getId(), library.getId()))
                .thenReturn(reservation);
        when(bookPresenceRepository.save(bookPresence)).thenReturn(bookPresence);

        assertEquals(bookPresence, bookPresenceServiceImpl.removeUserFromBook(user, library.getId(), book.getId()));

        verify(bookPresenceRepository, times(1))
                .findAllByLibraryIdAndBookIdAndUser(library.getId(), book.getId(), user);
        verify(journalService, times(1))
                .findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(bookPresence.getId(), user.getId());
        verify(journalService, times(1)).updateJournal(journal.getId(), journal);
        verify(bookPresenceRepository, times(1)).save(bookPresence);
    }

    @Test
    public void removeUserFromBook_BookPresenceNotFound() {
        when(bookPresenceRepository.findAllByLibraryIdAndBookIdAndUser(library.getId(), book.getId(), user))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> {bookPresenceServiceImpl.removeUserFromBook(user, library.getId(), book.getId());});

        verify(bookPresenceRepository, times(1))
                .findAllByLibraryIdAndBookIdAndUser(library.getId(), book.getId(), user);
        verify(journalService, never()).findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(anyLong(), anyLong());
        verify(journalService, never()).updateJournal(anyLong(), any(Journal.class));
        verify(bookPresenceRepository, never()).save(any(BookPresence.class));
    }

    @Test
    public void addBookToLibrary() {
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(libraryRepository.findById(book.getId())).thenReturn(Optional.of(library));
        when(bookPresenceRepository.save(any(BookPresence.class))).thenReturn(bookPresence);

        assertEquals(bookPresence, bookPresenceServiceImpl.addBookToLibrary(library.getId(), book.getId()));
        verify(bookRepository, times(1)).findById(book.getId());
        verify(libraryRepository, times(1)).findById(library.getId());
        verify(bookPresenceRepository, times(1)).save(any(BookPresence.class));
    }

    @Test
    public void getByBookId() {
        when(bookPresenceRepository.findAllByBookId(book.getId())).thenReturn(List.of(bookPresence));

        assertEquals(List.of(bookPresence), bookPresenceServiceImpl.getByBookId(book.getId()));
        verify(bookPresenceRepository, times(1)).findAllByBookId(book.getId());
    }

    @Test
    public void getByLibraryId() {
        when(bookPresenceRepository.findAllByLibraryId(library.getId())).thenReturn(List.of(bookPresence));

        assertEquals(List.of(bookPresence), bookPresenceServiceImpl.getByLibraryId(library.getId()));
        verify(bookPresenceRepository, times(1)).findAllByLibraryId(library.getId());
    }

    @Test
    public void getAllBookByLibraryIdAndBookId(){
        when(bookPresenceRepository
                .findAllByLibraryIdAndBookId(library.getId(), book.getId())).thenReturn(List.of(bookPresence));

        assertEquals(List.of(bookPresence),
                bookPresenceServiceImpl.getAllBookByLibraryIdAndBookId(library.getId(), book.getId()));
        verify(bookPresenceRepository, times(1))
                .findAllByLibraryIdAndBookId(library.getId(), book.getId());
    }

    @Test
    public void getAllBookByLibraryIdAndAvailability(){
        when(bookPresenceRepository
                .findAllByLibraryIdAndAvailability(library.getId(), Availability.AVAILABLE)).thenReturn(List.of(bookPresence));

        assertEquals(List.of(bookPresence),
                bookPresenceServiceImpl.getAllBookByLibraryIdAndAvailability(library.getId(), Availability.AVAILABLE));
        verify(bookPresenceRepository, times(1))
                .findAllByLibraryIdAndAvailability(library.getId(), Availability.AVAILABLE);
    }

    @Test
    public void findAllByLibraryIdAndBookIdAndAvailability(){
        when(bookPresenceRepository
                .findAllByLibraryIdAndBookIdAndAvailability(library.getId(), book.getId(), Availability.AVAILABLE))
                .thenReturn(List.of(bookPresence));

        assertEquals(List.of(bookPresence), bookPresenceServiceImpl
                .findAllByLibraryIdAndBookIdAndAvailability(library.getId(), book.getId(), Availability.AVAILABLE));
        verify(bookPresenceRepository, times(1))
                .findAllByLibraryIdAndBookIdAndAvailability(library.getId(), book.getId(), Availability.AVAILABLE);
    }

    @Test
    public void deleteBookPresenceByIdAndLibraryId(){
        doNothing().when(bookPresenceRepository).deleteBookPresenceByIdAndLibraryId(book.getId(), library.getId());

        bookPresenceServiceImpl.deleteBookPresenceByIdAndLibraryId(library.getId(), bookPresence.getId());
        verify(bookPresenceRepository, times(1))
                .deleteBookPresenceByIdAndLibraryId(book.getId(), library.getId());
    }

    @Test
    public void deleteBookPresenceById(){
        doNothing().when(bookPresenceRepository).deleteById(bookPresence.getId());

        bookPresenceServiceImpl.deleteBookPresenceById(bookPresence.getId());
        verify(bookPresenceRepository, times(1)).deleteById(bookPresence.getId());
    }
}