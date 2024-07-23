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
import com.example.librarymanagement.repository.BookPresenceRepository;
import com.example.librarymanagement.repository.UserRepository;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Mock
    private UserRepository userRepository;
    @Mock
    private BookPresenceRepository bookPresenceRepository;
    @Mock
    private BookPresenceServiceImpl bookPresenceServiceImpl;
    @Mock
    private ReservationServiceImpl reservationService;
    @Mock
    private JournalServiceImpl journalServiceImpl;

    private static Library library;
    private static Book book1;
    private static User user1;
    private static User user2;
    private static Journal journal1;
    private static Journal journal2;
    private static Reservation reservation1;
    private static Reservation reservation2;
    private static BookPresence bookPresence1;

    @BeforeAll
    public static void init() {
        library = new Library();
        library.setId(1L);
        library.setName("Library1");
        library.setAddress("Address1");

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

        user1 = new User();
        user1.setId(1L);
        user1.setFirstName("First");
        user1.setLastName("First");
        user1.setEmail("first@email.com");
        user1.setPhoneNumber("1234567890");
        user1.setPassword("Password1");

        user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Second");
        user2.setLastName("Second");
        user2.setEmail("second@email.com");
        user2.setPhoneNumber("0987654321");
        user2.setPassword("Password2");

        bookPresence1 = new BookPresence();
        bookPresence1.setId(1L);
        bookPresence1.setBook(book1);
        bookPresence1.setLibrary(library);
        bookPresence1.setUser(user1);
        bookPresence1.setAvailability(Availability.AVAILABLE);

        BookPresence bookPresence2 = new BookPresence();
        bookPresence2.setId(2L);
        bookPresence2.setBook(book2);
        bookPresence2.setLibrary(library);
        bookPresence2.setUser(user2);
        bookPresence2.setAvailability(Availability.NOT_AVAILABLE);

        library.setBookPresence(List.of(bookPresence1, bookPresence2));

        journal1 = new Journal();
        journal1.setId(1L);
        journal1.setBookPresence(bookPresence1);
        journal1.setUser(user1);
        journal1.setDateOfBorrowing(LocalDate.parse("2024-07-15"));
        journal1.setDateOfReturning(LocalDate.parse("2024-07-22"));

        journal2 = new Journal();
        journal2.setId(2L);
        journal2.setBookPresence(bookPresence2);
        journal2.setUser(user1);
        journal2.setDateOfBorrowing(LocalDate.parse("2024-07-15"));
        journal2.setDateOfReturning(LocalDate.parse("2024-07-22"));

        reservation1 = new Reservation();
        reservation1.setId(1L);
        reservation1.setBook(book1);
        reservation1.setUser(user1);
        reservation1.setLibrary(library);

        reservation2 = new Reservation();
        reservation2.setId(2L);
        reservation2.setBook(book2);
        reservation2.setUser(user1);
        reservation2.setLibrary(library);

        user1.setJournals(List.of(journal1, journal2));
        user1.setReservations(List.of(reservation1, reservation2));
        book1.setBookPresence(List.of(bookPresence1));
        bookPresence1.setJournals(List.of(journal1, journal2));
    }

    @Test
    public void getUserByPhoneNumberOrEmail() {
        when(userRepository.findByEmailOrPhoneNumber(user1.getEmail(), user1.getPhoneNumber())).thenReturn(Optional.of(user1));

        assertEquals(user1, userServiceImpl.getUserByPhoneNumberOrEmail(user1.getEmail(), user1.getPhoneNumber()));
        verify(userRepository, times(1)).findByEmailOrPhoneNumber(user1.getEmail(), user1.getPhoneNumber());
    }

    @Test
    public void getUserById() {
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));

        assertEquals(user1, userServiceImpl.getUserById(user1.getId()));
        verify(userRepository, times(1)).findById(user1.getId());
    }

    @Test
    public void createUser() {
        when(userRepository.existsByEmail(user1.getEmail())).thenReturn(false);
        when(userRepository.existsByPhoneNumber(user1.getPhoneNumber())).thenReturn(false);
        when(userRepository.save(user1)).thenReturn(user1);

        assertEquals(user1, userServiceImpl.createUser(user1));
        verify(userRepository, times(1)).save(user1);
    }

    @Test
    public void updateUser() {
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(userRepository.save(user1)).thenReturn(user1);

        assertEquals(user1, userServiceImpl.updateUser(user1.getId(), user1));
        verify(userRepository, times(1)).save(user1);
    }

    @Test
    public void deleteUser() {
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(bookPresenceRepository.findAllByUserId(user1.getId())).thenReturn(new ArrayList<>());
        doNothing().when(journalServiceImpl).deleteJournal(anyLong());
        doNothing().when(reservationService).deleteReservationById(anyLong());

        userServiceImpl.deleteUser(user1.getId());
        verify(userRepository, times(1)).delete(user1);
        verify(journalServiceImpl, times(1)).deleteJournal(user1.getId());
        verify(reservationService, times(1)).deleteReservationById(user1.getId());
    }

    @Test
    public void findAll(){
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        assertEquals(List.of(user1, user2), userServiceImpl.findAll());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void findJournalsByUser(){
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(journalServiceImpl.getJournalByUserId(user1.getId())).thenReturn(List.of(journal1, journal2));

        assertEquals(List.of(journal1, journal2), userServiceImpl.findJournalsByUser(user1.getId()));
        verify(journalServiceImpl, times(1)).getJournalByUserId(user1.getId());
    }

    @Test
    public void findReservationsByUser(){
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(reservationService.getReservationsByUserId(user1.getId())).thenReturn(List.of(reservation1, reservation2));

        assertEquals(List.of(reservation1, reservation2), userServiceImpl.findReservationsByUser(user1.getId()));
        verify(reservationService, times(1)).getReservationsByUserId(user1.getId());
    }

    @Test
    public void borrowBookFromLibrary(){
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(bookPresenceServiceImpl.addUserToBook(user1, library.getId(), book1.getId())).thenReturn(bookPresence1);

        assertEquals(List.of(journal1, journal2), userServiceImpl.borrowBookFromLibrary(user1.getId(), library.getId(), book1.getId()));
        verify(bookPresenceServiceImpl, times(1)).addUserToBook(user1, library.getId(), book1.getId());
    }

    @Test
    public void returnBookToLibrary(){
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(bookPresenceServiceImpl.removeUserFromBook(user1, library.getId(), book1.getId())).thenReturn(bookPresence1);

        assertEquals(List.of(journal1, journal2), userServiceImpl.returnBookToLibrary(user1.getId(), library.getId(), book1.getId()));
        verify(bookPresenceServiceImpl, times(1)).removeUserFromBook(user1, library.getId(), book1.getId());
    }

    @Test
    public void reserveBookInLibrary(){
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(reservationService.doReservationBook(user1, library.getId(), book1.getId())).thenReturn(List.of(reservation1));

        assertEquals(List.of(reservation1), userServiceImpl.reserveBookInLibrary(user1.getId(), library.getId(), book1.getId()));
        verify(reservationService, times(1)).doReservationBook(user1, library.getId(), book1.getId());
    }

    @Test
    public void cancelReservationInLibrary(){
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        userServiceImpl.cancelReservationInLibrary(user1.getId(), book1.getId());

        verify(reservationService, times(1)).removeReservation(user1, book1.getId());
    }
}

