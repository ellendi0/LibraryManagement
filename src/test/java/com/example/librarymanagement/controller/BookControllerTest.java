package com.example.librarymanagement.controller;

import com.example.librarymanagement.dto.BookPresenceDto;
import com.example.librarymanagement.dto.BookRequestDto;
import com.example.librarymanagement.dto.BookResponseDto;
import com.example.librarymanagement.dto.JournalDto;
import com.example.librarymanagement.dto.LibraryDto;
import com.example.librarymanagement.dto.ReservationDto;
import com.example.librarymanagement.dto.UserRequestDto;
import com.example.librarymanagement.dto.UserResponseDto;
import com.example.librarymanagement.exception.EntityNotFoundException;
import com.example.librarymanagement.model.entity.Author;
import com.example.librarymanagement.model.entity.Book;
import com.example.librarymanagement.model.entity.BookPresence;
import com.example.librarymanagement.model.entity.Journal;
import com.example.librarymanagement.model.entity.Library;
import com.example.librarymanagement.model.entity.Publisher;
import com.example.librarymanagement.model.entity.Reservation;
import com.example.librarymanagement.model.entity.User;
import com.example.librarymanagement.model.enums.Availability;
import com.example.librarymanagement.model.enums.Genre;
import com.example.librarymanagement.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    private static BookRequestDto bookRequestDto;
    private static BookResponseDto bookResponseDto;
    private static Author author;
    private static Publisher publisher;
    private static Book book1;


    @BeforeEach
    void setUp() {
        Library library = new Library();
        library.setId(1L);
        library.setName("Library 1");
        library.setAddress("Address 1");

        publisher = new Publisher();
        publisher.setId(1L);
        publisher.setName("Publisher1");

        author = new Author();
        author.setId(1L);
        author.setFirstName("Author1");
        author.setLastName("Author1");
        author.setPseudonym("Author1");

        book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Book1");
        book1.setAuthor(author);
        book1.setPublishedYear(2021);
        book1.setPublisher(publisher);
        book1.setGenre(Genre.DRAMA);
        book1.setIsbn(1234567890123L);

        User user1 = new User();
        user1.setId(1L);
        user1.setFirstName("First");
        user1.setLastName("First");
        user1.setEmail("first@email.com");
        user1.setPhoneNumber("1234567890");
        user1.setPassword("Password1");

        BookPresence bookPresence = new BookPresence();
        bookPresence.setBook(book1);
        bookPresence.setLibrary(library);
        bookPresence.setUser(user1);
        bookPresence.setAvailability(Availability.AVAILABLE);

        library.setBookPresence(List.of(bookPresence));

        Journal jornal = new Journal();
        jornal.setId(1L);
        jornal.setBookPresence(bookPresence);
        jornal.setUser(user1);
        jornal.setDateOfBorrowing(LocalDate.parse("2024-07-15"));
        jornal.setDateOfReturning(LocalDate.parse("2024-07-22"));

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setBook(book1);
        reservation.setUser(user1);
        reservation.setLibrary(library);

        user1.setJournals(List.of(jornal));
        user1.setReservations(List.of(reservation));
        book1.setBookPresence(List.of(bookPresence));
        book1.setReservations(List.of(reservation));
        bookPresence.setJournals(List.of(jornal));

        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setFirstName("First");
        userRequestDto.setLastName("First");
        userRequestDto.setEmail("first@example.com");
        userRequestDto.setPlainPassword("Password1");
        userRequestDto.setPhoneNumber("1234567890");

        bookRequestDto = new BookRequestDto();
        bookRequestDto.setTitle("Book1");
        bookRequestDto.setAuthorId(1L);
        bookRequestDto.setPublisherId(1L);
        bookRequestDto.setPublishedYear(2021);
        bookRequestDto.setGenre("Drama");
        bookRequestDto.setIsbn(1234567890123L);

        bookResponseDto = new BookResponseDto(book1);
    }

    @Test
    void createBook() throws Exception {
        String expected = objectMapper.writeValueAsString(bookResponseDto);

        given(bookService.createBook(anyLong(), anyLong(), any(Book.class))).willReturn(book1);

        String result = mockMvc.perform(post("/api/v1/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequestDto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        assertEquals(expected, result);
    }

    @Test
    void createBookWithNull() throws Exception {
        bookRequestDto.setTitle(null);

        mockMvc.perform(post("/api/v1/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createBookWithEmpty() throws Exception {
        bookRequestDto.setTitle("");

        mockMvc.perform(post("/api/v1/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createBookWithInvalidGenre() throws Exception {
        bookRequestDto.setGenre("Invalid");

        mockMvc.perform(post("/api/v1/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateBook() throws Exception {
        Book updatedBook = new Book();
        updatedBook.setId(1L);
        updatedBook.setTitle("Book1");
        updatedBook.setAuthor(author);
        updatedBook.setPublishedYear(2021);
        updatedBook.setPublisher(publisher);
        updatedBook.setGenre(Genre.DRAMA);
        updatedBook.setIsbn(1234599990123L);

        BookRequestDto bookRequestDto1 = new BookRequestDto();
        bookRequestDto1.setTitle("Book1");
        bookRequestDto1.setAuthorId(1L);
        bookRequestDto1.setPublisherId(1L);
        bookRequestDto1.setPublishedYear(2021);
        bookRequestDto1.setGenre("Drama");
        bookRequestDto1.setIsbn(1234599990123L);

        String expected = objectMapper.writeValueAsString(new BookResponseDto(updatedBook));

        given(bookService.updateBook(anyLong(), any(Book.class))).willReturn(updatedBook);

        String result = mockMvc.perform(put("/api/v1/book/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequestDto1)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(expected, result);
    }

    @Test
    void updateBookWithNull() throws Exception {
        bookRequestDto.setTitle(null);

        mockMvc.perform(put("/api/v1/book/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateBookWithEmpty() throws Exception {
        bookRequestDto.setTitle("");

        mockMvc.perform(put("/api/v1/book/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getBookById() throws Exception {
        String expected = objectMapper.writeValueAsString(bookResponseDto);

        given(bookService.getBookById(anyLong())).willReturn(book1);

        String result = mockMvc.perform(get("/api/v1/book/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(expected, result);
    }

    @Test
    void getBookByNonExistingId() throws Exception {
        given(bookService.getBookById(anyLong())).willThrow(new EntityNotFoundException("Book"));

        mockMvc.perform(get("/api/v1/book/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllBooks() throws Exception {
        List<Book> books = List.of(book1);
        List<BookResponseDto> bookResponseDtos = List.of(bookResponseDto);

        String expected = objectMapper.writeValueAsString(bookResponseDtos);

        given(bookService.findAll()).willReturn(books);

        String result = mockMvc.perform(get("/api/v1/book/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(expected, result);
    }

    @Test
    void deleteBook() throws Exception {
        mockMvc.perform(delete("/api/v1/book/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
