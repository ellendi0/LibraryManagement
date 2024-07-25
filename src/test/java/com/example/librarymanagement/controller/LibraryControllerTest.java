package com.example.librarymanagement.controller;

import com.example.librarymanagement.dto.BookPresenceDto;
import com.example.librarymanagement.dto.ErrorDto;
import com.example.librarymanagement.dto.LibraryDto;
import com.example.librarymanagement.dto.UserRequestDto;
import com.example.librarymanagement.dto.mapper.BookPresenceMapper;
import com.example.librarymanagement.dto.mapper.ErrorMapper;
import com.example.librarymanagement.dto.mapper.LibraryMapper;
import com.example.librarymanagement.dto.mapper.UserMapper;
import com.example.librarymanagement.exception.EntityNotFoundException;
import com.example.librarymanagement.exception.GlobalExceptionHandler;
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
import com.example.librarymanagement.service.LibraryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LibraryController.class)
public class LibraryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LibraryService libraryService;

    @MockBean
    private LibraryMapper libraryMapper;

    @MockBean
    private ErrorMapper errorMapper;

    @MockBean
    private BookPresenceMapper bookPresenceMapper;

    @MockBean
    private GlobalExceptionHandler globalExceptionHandler;

    @Autowired
    private ObjectMapper objectMapper;

    private static BookPresenceDto bookPresenceDto;
    private static ErrorMapper errorMapper1;
    private static LibraryDto libraryDto;
    private static Library library;
    private static BookPresence bookPresence;
    private static ErrorDto errorDto1;
    private static ErrorDto errorDto2;

    @BeforeEach
    void setUp() {
        BookPresenceMapper bookPresenceMapper1 = new BookPresenceMapper(new UserMapper());
        LibraryMapper libraryMapper1 = new LibraryMapper();
        ErrorMapper errorMapper1 = new ErrorMapper();

        library = new Library();
        library.setName("Library 1");
        library.setAddress("Address 1");

        Publisher publisher = new Publisher();
        publisher.setName("Publisher1");

        Author author = new Author();
        author.setFirstName("Author1");
        author.setLastName("Author1");

        Book book1 = new Book();
        book1.setTitle("Book1");
        book1.setAuthor(author);
        book1.setPublishedYear(2021);
        book1.setPublisher(publisher);
        book1.setGenre(Genre.DRAMA);
        book1.setIsbn(1234567890123L);

        User user1 = new User();
        user1.setFirstName("First");
        user1.setLastName("First");
        user1.setEmail("first@email.com");
        user1.setPhoneNumber("1234567890");
        user1.setPassword("Password1");

        bookPresence = new BookPresence();
        bookPresence.setBook(book1);
        bookPresence.setLibrary(library);
        bookPresence.setUser(user1);
        bookPresence.setAvailability(Availability.AVAILABLE);

        library.setBookPresence(List.of(bookPresence));

        Journal journal = new Journal();
        journal.setUser(user1);
        journal.setBookPresence(bookPresence);
        journal.setDateOfBorrowing(LocalDate.parse("2024-07-15"));
        journal.setDateOfReturning(LocalDate.parse("2024-07-22"));

        Reservation reservation = new Reservation(user1, book1, library);

        user1.setJournals(List.of(journal));
        user1.setReservations(List.of(reservation));
        book1.setBookPresence(List.of(bookPresence));
        book1.setReservations(List.of(reservation));
        bookPresence.setJournals(List.of(journal));

        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setFirstName("First");
        userRequestDto.setLastName("First");
        userRequestDto.setEmail("first@example.com");
        userRequestDto.setPhoneNumber("1234567890");

        libraryDto = libraryMapper1.toLibraryDto(library);
        bookPresenceDto = bookPresenceMapper1.toBookPresenceDto(bookPresence);
        errorDto1 = errorMapper1.toErrorDto(HttpStatus.NOT_FOUND, "Library");
        errorDto2 = errorMapper1.toErrorDto(HttpStatus.BAD_REQUEST, "Invalid data");
    }

    @Test
    void createLibrary() throws Exception {
        String expected = objectMapper.writeValueAsString(libraryDto);

        given(libraryService.createLibrary(any(Library.class))).willReturn(library);
        given(libraryMapper.toLibraryDto((Library) any())).willReturn(libraryDto);

        String result = mockMvc.perform(post("/api/v1/library")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(libraryDto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        assertEquals(expected, result);
    }


    @Test
    void createLibraryWithInvalidData() throws Exception {
        libraryDto.setName("");
        libraryDto.setAddress("");

        given(libraryService.createLibrary(any(Library.class))).willThrow(new IllegalArgumentException());
        given(errorMapper.toErrorDto(any(), (List<String>) any())).willReturn(errorDto2);

        mockMvc.perform(post("/api/v1/library")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(libraryDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateLibrary() throws Exception {
        String expected = objectMapper.writeValueAsString(libraryDto);

        given(libraryService.updateLibrary(anyLong(), any(Library.class))).willReturn(library);
        given(libraryMapper.toLibraryDto((Library) any())).willReturn(libraryDto);

        String result = mockMvc.perform(put("/api/v1/library/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(libraryDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(expected, result);

    }

    @Test
    void updateLibraryWithInvalidData() throws Exception {
        libraryDto.setName("");
        libraryDto.setAddress("");

        given(libraryService.updateLibrary(anyLong(), any(Library.class))).willThrow(new IllegalArgumentException());
        given(errorMapper.toErrorDto(any(), (List<String>) any())).willReturn(errorDto2);

        mockMvc.perform(put("/api/v1/library/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(libraryDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addBookToLibrary() throws Exception {
        String expected = objectMapper.writeValueAsString(libraryDto);

        given(libraryService.addBookToLibrary(anyLong(), anyLong())).willReturn(library);
        given(libraryMapper.toLibraryDto((Library) any())).willReturn(libraryDto);

        String result = mockMvc.perform(post("/api/v1/library/{id}/contents", 1L)
                        .param("bookId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        assertEquals(expected, result);

    }

    @Test
    void addBookToLibraryWithInvalidData() throws Exception {
        given(libraryService.addBookToLibrary(anyLong(), anyLong())).willThrow(new EntityNotFoundException("Book"));
        given(errorMapper.toErrorDto(any(), (List<String>) any())).willReturn(errorDto1);

        mockMvc.perform(post("/api/v1/library/{id}/contents", 1L)
                        .param("bookId", "0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllBooksByLibraryIdAndStatus() throws Exception {
        String expected = objectMapper.writeValueAsString(List.of(bookPresenceDto));

        given(libraryService.getAllBooksByLibraryIdAndAvailability(anyLong(), any(Availability.class)))
                .willReturn(List.of(bookPresence));
        given(bookPresenceMapper.toBookPresenceDto((List<BookPresence>) any())).willReturn(List.of(bookPresenceDto));

        String result = mockMvc.perform(get("/api/v1/library/{id}/contents", 1L)
                        .param("availability", "AVAILABLE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(expected, result);

    }

    @Test
    void getAllBooksByLibraryIdAndStatusWithInvalidData() throws Exception {
        given(libraryService.getAllBooksByLibraryIdAndAvailability(anyLong(), any(Availability.class)))
                .willThrow(new EntityNotFoundException("Library"));
        given(errorMapper.toErrorDto(any(), (List<String>) any())).willReturn(errorDto1);

        mockMvc.perform(get("/api/v1/library/{id}/contents", 0L)
                        .param("availability", "AVAILABLE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllBooksByLibraryId() throws Exception {
        String expected = objectMapper.writeValueAsString(List.of(bookPresenceDto));

        given(libraryService.getAllBooksByLibraryId(anyLong())).willReturn(List.of(bookPresence));
        given(bookPresenceMapper.toBookPresenceDto((List<BookPresence>) any())).willReturn(List.of(bookPresenceDto));

        String result = mockMvc.perform(get("/api/v1/library/{id}/all", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(expected, result);
    }

    @Test
    void getAllBooksByLibraryIdWithInvalidData() throws Exception {
        given(libraryService.getAllBooksByLibraryId(anyLong())).willThrow(new EntityNotFoundException("Library"));
        given(errorMapper.toErrorDto(any(), (List<String>) any())).willReturn(errorDto1);

        mockMvc.perform(get("/api/v1/library/{id}/all", 0L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteLibrary() throws Exception {
        willDoNothing().given(libraryService).deleteLibrary(anyLong());
        given(libraryMapper.toLibraryDto((Library) any())).willReturn(libraryDto);

        mockMvc.perform(delete("/api/v1/library/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void removeBookFromLibrary() throws Exception {
        willDoNothing().given(libraryService).removeBookFromLibrary(anyLong(), anyLong());

        mockMvc.perform(delete("/api/v1/library/{id}/contents", 1L)
                        .param("bookId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}