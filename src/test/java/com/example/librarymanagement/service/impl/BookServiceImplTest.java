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
import com.example.librarymanagement.repository.BookRepository;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

    @InjectMocks
    private BookServiceImpl bookServiceImpl;

    @Mock
    private BookRepository bookRepository;
    @Mock
    private AuthorServiceImpl authorServiceImpl;
    @Mock
    private PublisherServiceImpl publisherServiceImpl;

    private static Author author1;
    private static Publisher publisher1;
    private static Book book1;
    private static Book book2;

    @BeforeAll
    public static void init() {
        Library library1 = new Library();
        library1.setId(1L);
        library1.setName("Library1");
        library1.setAddress("Address1");

        Library library2 = new Library();
        library2.setId(2L);
        library2.setName("Library2");
        library2.setAddress("Address2");

        publisher1 = new Publisher();
        publisher1.setId(1L);
        publisher1.setName("Publisher1");

        Publisher publisher2 = new Publisher();
        publisher2.setId(2L);
        publisher2.setName("Publisher2");

        author1 = new Author();
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

        book2 = new Book();
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

        BookPresence bookPresence1 = new BookPresence();
        bookPresence1.setId(1L);
        bookPresence1.setBook(book1);
        bookPresence1.setLibrary(library1);
        bookPresence1.setUser(user1);
        bookPresence1.setAvailability(Availability.AVAILABLE);

        BookPresence bookPresence2 = new BookPresence();
        bookPresence2.setId(2L);
        bookPresence2.setBook(book2);
        bookPresence2.setLibrary(library1);
        bookPresence2.setUser(user2);
        bookPresence2.setAvailability(Availability.NOT_AVAILABLE);

        Journal journal1 = new Journal();
        journal1.setId(1L);
        journal1.setBookPresence(bookPresence1);
        journal1.setUser(user1);
        journal1.setDateOfBorrowing(LocalDate.parse("2024-07-15"));
        journal1.setDateOfReturning(LocalDate.parse("2024-07-22"));

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
        user1.setJournals(List.of(journal1, jornal2));
        user1.setReservations(List.of(reservation1, reservation2));
        bookPresence1.setJournals(List.of(journal1, jornal2));
        bookPresence1.setUser(user1);
    }

    @Test
    public void findAll() {
        when(bookRepository.findAll()).thenReturn(List.of(book1, book2));

        assertEquals(List.of(book1, book2), bookServiceImpl.findAll());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    public void getBookById() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));

        assertEquals(book1, bookServiceImpl.getBookById(1L));
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    public void getBookByTitleAndAuthor() {
        when(bookRepository.findBookByTitleAndAuthorId("Book1", 1L)).thenReturn(Optional.of(book1));

        assertEquals(book1, bookServiceImpl.getBookByTitleAndAuthor("Book1", 1L));
        verify(bookRepository, times(1)).findBookByTitleAndAuthorId("Book1", 1L);
    }

    @Test
    public void createBook() {
        when(authorServiceImpl.getAuthorById(1L)).thenReturn(author1);
        when(publisherServiceImpl.getPublisherById(1L)).thenReturn(publisher1);
        when(bookRepository.save(book1)).thenReturn(book1);

        assertEquals(book1, bookServiceImpl.createBook(1L, 1L, book1));
        verify(bookRepository, times(1)).save(book1);
    }

    @Test
    public void updateBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        when(bookRepository.save(book1)).thenReturn(book1);

        assertEquals(book1, bookServiceImpl.updateBook(1L, book1));
        verify(bookRepository, times(1)).save(book1);
    }

    @Test
    public void deleteBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        doNothing().when(bookRepository).delete(book1);

        bookServiceImpl.deleteBook(1L);
        verify(bookRepository, times(1)).delete(book1);
        verify(bookRepository, times(1)).findById(1L);
    }
}