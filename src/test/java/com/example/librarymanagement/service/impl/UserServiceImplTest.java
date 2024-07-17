package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.data.TestDataFactory;
import com.example.librarymanagement.model.entity.Book;
import com.example.librarymanagement.model.entity.BookPresence;
import com.example.librarymanagement.model.entity.Journal;
import com.example.librarymanagement.model.entity.Library;
import com.example.librarymanagement.model.entity.Reservation;
import com.example.librarymanagement.model.entity.User;
import com.example.librarymanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    private BookPresenceServiceImpl bookPresenceServiceImpl;
    @Mock
    private ReservationServiceImpl reservationService;
    @Mock
    private JournalServiceImpl journalServiceImpl;

    private static Library library;
    private static Book book1;
    private static User user1;
    private static Journal journal1;
    private static Reservation reservation1;
    private static BookPresence bookPresence1;

    @BeforeAll
    public static void init() {
        TestDataFactory.TestDataRel data = TestDataFactory.createTestDataRel();

        library = data.library;
        user1 = data.user;
        book1 = data.book;
        bookPresence1 = data.bookPresence;
        journal1 = data.journal;
        reservation1 = data.reservation;
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
    public void findAll(){
        when(userRepository.findAll()).thenReturn(List.of(user1));

        assertEquals(List.of(user1), userServiceImpl.findAll());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void findJournalsByUser(){
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(journalServiceImpl.getJournalByUserId(user1.getId())).thenReturn(List.of(journal1));

        assertEquals(List.of(journal1), userServiceImpl.findJournalsByUser(user1.getId()));
        verify(journalServiceImpl, times(1)).getJournalByUserId(user1.getId());
    }

    @Test
    public void findReservationsByUser(){
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(reservationService.getReservationsByUserId(user1.getId())).thenReturn(List.of(reservation1));

        assertEquals(List.of(reservation1), userServiceImpl.findReservationsByUser(user1.getId()));
        verify(reservationService, times(1)).getReservationsByUserId(user1.getId());
    }

    @Test
    public void borrowBookFromLibrary(){
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(bookPresenceServiceImpl.addUserToBook(user1, library.getId(), book1.getId())).thenReturn(bookPresence1);

        assertEquals(List.of(journal1), userServiceImpl.borrowBookFromLibrary(user1.getId(), library.getId(), book1.getId()));
        verify(bookPresenceServiceImpl, times(1)).addUserToBook(user1, library.getId(), book1.getId());
    }

    @Test
    public void returnBookToLibrary(){
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(bookPresenceServiceImpl.removeUserFromBook(user1, library.getId(), book1.getId())).thenReturn(bookPresence1);

        assertEquals(List.of(journal1), userServiceImpl.returnBookToLibrary(user1.getId(), library.getId(), book1.getId()));
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