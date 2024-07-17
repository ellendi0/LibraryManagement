package com.example.librarymanagement.data;

import com.example.librarymanagement.dto.BookRequestDto;
import com.example.librarymanagement.dto.UserRequestDto;
import com.example.librarymanagement.model.entity.Library;
import com.example.librarymanagement.model.entity.Publisher;
import com.example.librarymanagement.model.entity.Author;
import com.example.librarymanagement.model.entity.Book;
import com.example.librarymanagement.model.entity.User;
import com.example.librarymanagement.model.entity.BookPresence;
import com.example.librarymanagement.model.entity.Journal;
import com.example.librarymanagement.model.entity.Reservation;
import com.example.librarymanagement.model.enums.Availability;
import com.example.librarymanagement.model.enums.Genre;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TestDataFactory {

    public static Library createLibrary() {
        Library library = new Library();
        library.setId(1L);
        library.setName("Library1");
        library.setAddress("Address1");
        return library;
    }

    public static Publisher createPublisher() {
        Publisher publisher = new Publisher();
        publisher.setId(1L);
        publisher.setName("Publisher1");
        return publisher;
    }

    public static Author createAuthor() {
        Author author = new Author();
        author.setId(1L);
        author.setFirstName("Author");
        author.setLastName("Author");
        return author;
    }

    public static Book createBook() {
        Author author = createAuthor();
        Publisher publisher = createPublisher();
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Book1");
        book.setAuthor(author);
        book.setPublishedYear(2021);
        book.setPublisher(publisher);
        book.setGenre(Genre.DRAMA);
        book.setIsbn(1234567890123L);
        return book;
    }

    public static User createUser() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("First");
        user.setLastName("First");
        user.setEmail("first@email.com");
        user.setPhoneNumber("1234567890");
        user.setPassword("Password1");
        return user;
    }

    public static BookPresence createBookPresence() {
        Book book = createBook();
        Library library = createLibrary();
        User user = createUser();
        BookPresence bookPresence = new BookPresence();
        bookPresence.setId(1L);
        bookPresence.setBook(book);
        bookPresence.setLibrary(library);
        bookPresence.setUser(user);
        bookPresence.setAvailability(Availability.AVAILABLE);
        return bookPresence;
    }

    public static Journal createJournal(User user, BookPresence bookPresence) {
        Journal journal = new Journal();
        journal.setId(1L);
        journal.setBookPresence(bookPresence);
        journal.setUser(user);
        journal.setDateOfBorrowing(LocalDate.parse("2024-07-15"));
        journal.setDateOfReturning(LocalDate.parse("2024-07-22"));
        return journal;
    }

    public static Reservation createReservation(Book book, User user, Library library) {
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setBook(book);
        reservation.setUser(user);
        reservation.setLibrary(library);
        return reservation;
    }

    public static UserRequestDto createUserRequestDto() {
        UserRequestDto dto = new UserRequestDto();
        dto.setFirstName("First");
        dto.setLastName("First");
        dto.setEmail("first@example.com");
        dto.setPhoneNumber("1234567890");
        dto.setPassword("Password1");
        return dto;
    }

    public static BookRequestDto createBookRequestDto() {
        BookRequestDto dto = new BookRequestDto();
        dto.setTitle("Book1");
        dto.setAuthorId(1L);
        dto.setPublisherId(1L);
        dto.setPublishedYear(2021);
        dto.setGenre(Genre.DRAMA);
        dto.setIsbn(1234567890123L);
        return dto;
    }

    public static TestDataRel createTestDataRel() {
        Library library = createLibrary();
        User user = createUser();
        Book book = createBook();
        BookPresence bookPresence = createBookPresence();
        Journal journal = createJournal(user, bookPresence);
        Reservation reservation = createReservation(book, user, library);

        user.setJournals(new ArrayList<>(List.of(journal)));
        user.setReservations(List.of(reservation));
        book.setBookPresence(new ArrayList<>(List.of(bookPresence)));
        book.setReservations(List.of(reservation));
        bookPresence.setJournals(new ArrayList<>(List.of(journal)));
        bookPresence.setUser(user);
        library.setBookPresence(new ArrayList<>(List.of(bookPresence)));

        return new TestDataRel(library, user, book, bookPresence, journal, reservation);
    }

    public static class TestDataRel {
        public Library library;
        public User user;
        public Book book;
        public BookPresence bookPresence;
        public Journal journal;
        public Reservation reservation;

        public TestDataRel(Library library, User user, Book book, BookPresence bookPresence, Journal journal, Reservation reservation) {
            this.library = library;
            this.user = user;
            this.book = book;
            this.bookPresence = bookPresence;
            this.journal = journal;
            this.reservation = reservation;
        }
    }
}