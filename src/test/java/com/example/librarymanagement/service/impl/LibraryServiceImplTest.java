package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.model.entity.Author;
import com.example.librarymanagement.model.entity.Book;
import com.example.librarymanagement.model.entity.BookPresence;
import com.example.librarymanagement.model.entity.Journal;
import com.example.librarymanagement.model.entity.Library;
import com.example.librarymanagement.model.entity.Publisher;
import com.example.librarymanagement.model.entity.Reservation;
import com.example.librarymanagement.model.entity.User;
import com.example.librarymanagement.model.enums.Availability;
import com.example.librarymanagement.repository.LibraryRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LibraryServiceImplTest {

    @InjectMocks
    private LibraryServiceImpl libraryServiceImpl;

    @Mock
    private LibraryRepository libraryRepository;
    @Mock
    private BookPresenceServiceImpl bookPresenceServiceImpl;
    @Mock
    private BookServiceImpl bookServiceImpl;
    @Mock
    private ReservationServiceImpl reservationService;
    @Mock
    private JournalServiceImpl journalServiceImpl;

    private static Library library1;
    private static Library library2;
    private static Book book1;
    private static BookPresence bookPresence1;
    private static BookPresence bookPresence2;

    @BeforeAll
    public static void init() {
        library1 = new Library();
        library1.setId(1L);
        library1.setName("Library1");
        library1.setAddress("Address1");

        library2 = new Library();
        library2.setId(2L);
        library2.setName("Library2");
        library2.setAddress("Address2");

        Publisher publisher1 = new Publisher();
        publisher1.setId(1L);
        publisher1.setName("Publisher1");

        Publisher publisher2 = new Publisher();
        publisher2.setId(2L);
        publisher2.setName("Publisher2");

        Author author1 = new Author();
        author1.setId(1L);
        author1.setFirstName("Author1");
        author1.setLastName("Author1");
        author1.setPseudonym("Author1");

        Author author2 = new Author();
        author2.setId(2L);
        author2.setFirstName("Author2");
        author2.setLastName("Author2");
        author2.setPseudonym("Author2");

        book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Book1");
        book1.setAuthor(author1);
        book1.setPublishedYear(2021);
        book1.setPublisher(publisher1);

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Book2");
        book2.setAuthor(author2);
        book2.setPublishedYear(2022);
        book2.setPublisher(publisher2);

        User user1 = new User();
        user1.setId(1L);
        user1.setFirstName("First");
        user1.setLastName("First");
        user1.setEmail("first@email.com");
        user1.setPhoneNumber("1234567890");
        user1.setPassword("Password1");

        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Second");
        user2.setLastName("Second");
        user2.setEmail("second@email.com");
        user2.setPhoneNumber("0987654321");
        user2.setPassword("Password2");

        bookPresence1 = new BookPresence();
        bookPresence1.setId(1L);
        bookPresence1.setBook(book1);
        bookPresence1.setLibrary(library1);
        bookPresence1.setUser(user1);
        bookPresence1.setAvailability(Availability.AVAILABLE);

        bookPresence2 = new BookPresence();
        bookPresence2.setId(2L);
        bookPresence2.setBook(book2);
        bookPresence2.setLibrary(library1);
        bookPresence2.setUser(user2);
        bookPresence2.setAvailability(Availability.NOT_AVAILABLE);

        Journal jornal1 = new Journal();
        jornal1.setId(1L);
        jornal1.setBookPresence(bookPresence1);
        jornal1.setUser(user1);
        jornal1.setDateOfBorrowing(LocalDate.parse("2024-07-15"));
        jornal1.setDateOfReturning(LocalDate.parse("2024-07-22"));

        Journal jornal2 = new Journal();
        jornal2.setId(2L);
        jornal2.setBookPresence(bookPresence2);
        jornal2.setUser(user1);
        jornal2.setDateOfBorrowing(LocalDate.parse("2024-07-15"));
        jornal2.setDateOfReturning(LocalDate.parse("2024-07-22"));

        Reservation reservation1 = new Reservation();
        reservation1.setId(1L);
        reservation1.setBook(book1);
        reservation1.setUser(user1);
        reservation1.setLibrary(library1);

        Reservation reservation2 = new Reservation();
        reservation2.setId(2L);
        reservation2.setBook(book2);
        reservation2.setUser(user1);
        reservation2.setLibrary(library1);

        book1.setBookPresence(new ArrayList<>());
        library1.setBookPresence(new ArrayList<>());
        user1.setJournals(List.of(jornal1, jornal2));
        user1.setReservations(List.of(reservation1, reservation2));
        bookPresence1.setJournals(List.of(jornal1, jornal2));
        bookPresence1.setUser(user1);
    }

    @Test
    public void testFindAll() {
        when(libraryRepository.findAll()).thenReturn(List.of(library1, library2));

        assertEquals(List.of(library1, library2), libraryServiceImpl.findAll());
        verify(libraryRepository, times(1)).findAll();
    }

    @Test
    public void testGetLibraryById() {
        when(libraryRepository.findById(anyLong())).thenReturn(Optional.of(library1));

        assertEquals(library1, libraryServiceImpl.getLibraryById(1L));
        verify(libraryRepository, times(1)).findById(1L);
    }

    @Test
    public void testCreateLibrary() {
        when(libraryRepository.save(library1)).thenReturn(library1);

        assertEquals(library1, libraryServiceImpl.createLibrary(library1));
        verify(libraryRepository, times(1)).save(library1);
    }

    @Test
    public void testUpdateLibrary() {
        when(libraryRepository.findById(anyLong())).thenReturn(Optional.of(library1));
        when(libraryRepository.save(library1)).thenReturn(library1);

        assertEquals(library1, libraryServiceImpl.updateLibrary(1L, library1));
        verify(libraryRepository, times(1)).findById(1L);
        verify(libraryRepository, times(1)).save(library1);
    }

    @Test
    public void testGetAllBooksByLibraryIdAndAvailability() {
        when(bookPresenceServiceImpl.getAllBookByLibraryIdAndAvailability(1L, Availability.AVAILABLE)).thenReturn(List.of(bookPresence1));

        assertEquals(List.of(bookPresence1), libraryServiceImpl.getAllBooksByLibraryIdAndAvailability(1L, Availability.AVAILABLE));
        verify(bookPresenceServiceImpl, times(1)).getAllBookByLibraryIdAndAvailability(1L, Availability.AVAILABLE);
    }

    @Test
    public void testGetAllBooksByLibraryId() {
        when(bookPresenceServiceImpl.getByLibraryId(1L)).thenReturn(List.of(bookPresence1, bookPresence2));

        assertEquals(List.of(bookPresence1, bookPresence2), libraryServiceImpl.getAllBooksByLibraryId(1L));
        verify(bookPresenceServiceImpl, times(1)).getByLibraryId(1L);
    }

    @Test
    public void testAddBookToLibrary() {
        when(libraryRepository.findById(1L)).thenReturn(Optional.of(library1));
        when(bookServiceImpl.getBookById(1L)).thenReturn(book1);
        when(libraryRepository.save(library1)).thenReturn(library1);
        when(bookPresenceServiceImpl.createBookPresence(any(BookPresence.class))).thenReturn(bookPresence1);

        assertEquals(library1, libraryServiceImpl.addBookToLibrary(1L, 1L));
        verify(libraryRepository, times(1)).findById(1L);
        verify(bookServiceImpl, times(1)).getBookById(1L);
        verify(bookPresenceServiceImpl, times(1)).createBookPresence(any(BookPresence.class));
        verify(libraryRepository, times(1)).save(library1);
    }

    @Test
    public void testRemoveBookFromLibrary() {
        when(libraryRepository.findById(1L)).thenReturn(Optional.of(library1));
        when(bookPresenceServiceImpl.getById(1L)).thenReturn(bookPresence1);
        doNothing().when(bookPresenceServiceImpl).deleteBookPresenceById(1L);

        libraryServiceImpl.removeBookFromLibrary(1L, 1L);
        verify(libraryRepository, times(1)).findById(1L);
        verify(bookPresenceServiceImpl, times(1)).getById(1L);
        verify(bookPresenceServiceImpl, times(1)).deleteBookPresenceById(1L);
    }

    @Test
    public void deleteLibrary() {
        when(libraryRepository.findById(1L)).thenReturn(Optional.of(library1));
        doNothing().when(libraryRepository).deleteById(1L);

        libraryServiceImpl.deleteLibrary(1L);
        verify(libraryRepository, times(1)).deleteById(1L);
    }
}