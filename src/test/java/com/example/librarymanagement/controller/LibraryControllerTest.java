package com.example.librarymanagement.controller;

import com.example.librarymanagement.dto.BookPresenceDto;
import com.example.librarymanagement.dto.LibraryDto;
import com.example.librarymanagement.dto.UserRequestDto;
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
import com.example.librarymanagement.service.LibraryService;
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

    @Autowired
    private ObjectMapper objectMapper;

    private BookPresenceDto bookPresenceDto;
    private LibraryDto libraryDto;
    private static Library library;
    private static BookPresence bookPresence;


    @BeforeEach
    void setUp() {
        library = new Library();
        library.setId(1L);
        library.setName("Library 1");
        library.setAddress("Address 1");

        Publisher publisher = new Publisher();
        publisher.setId(1L);
        publisher.setName("Publisher1");

        Author author = new Author();
        author.setId(1L);
        author.setFirstName("Author1");
        author.setLastName("Author1");
        author.setPseudonym("Author1");

        Book book1 = new Book();
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

        bookPresence = new BookPresence();
        bookPresence.setId(1L);
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

        libraryDto = new LibraryDto(library);
        bookPresenceDto = new BookPresenceDto(bookPresence);
    }

    @Test
    void createLibrary() throws Exception {
        String expected = objectMapper.writeValueAsString(libraryDto);

        given(libraryService.createLibrary(any(Library.class))).willReturn(library);

        String result = mockMvc.perform(post("/api/v1/library")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(libraryDto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        assert expected.equals(result);
    }

    @Test
    void createLibraryWithInvalidData() throws Exception {
        libraryDto.setName("");
        libraryDto.setAddress("");

        mockMvc.perform(post("/api/v1/library")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(libraryDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateLibrary() throws Exception {
        String expected = objectMapper.writeValueAsString(libraryDto);

        given(libraryService.updateLibrary(anyLong(), any(Library.class))).willReturn(library);

        String result = mockMvc.perform(put("/api/v1/library/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(libraryDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assert expected.equals(result);
    }

    @Test
    void updateLibraryWithInvalidData() throws Exception {
        libraryDto.setName("");
        libraryDto.setAddress("");

        mockMvc.perform(put("/api/v1/library/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(libraryDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addBookToLibrary() throws Exception {
        String expected = objectMapper.writeValueAsString(libraryDto);

        given(libraryService.addBookToLibrary(anyLong(), anyLong())).willReturn(library);

        String result = mockMvc.perform(post("/api/v1/library/{id}/contents", 1L)
                        .param("bookId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assert expected.equals(result);
    }

    @Test
    void addBookToLibraryWithInvalidData() throws Exception {
        given(libraryService.addBookToLibrary(anyLong(), anyLong())).willThrow(new EntityNotFoundException("Book"));

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

        String result = mockMvc.perform(get("/api/v1/library/{id}/contents", 1L)
                        .param("availability", "AVAILABLE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assert expected.equals(result);
    }

    @Test
    void getAllBooksByLibraryIdAndStatusWithInvalidData() throws Exception {
        given(libraryService.getAllBooksByLibraryIdAndAvailability(anyLong(), any(Availability.class)))
                .willThrow(new EntityNotFoundException("Library"));

        mockMvc.perform(get("/api/v1/library/{id}/contents", 0L)
                        .param("availability", "AVAILABLE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllBooksByLibraryId() throws Exception {
        String expected = objectMapper.writeValueAsString(List.of(bookPresenceDto));

        given(libraryService.getAllBooksByLibraryId(anyLong())).willReturn(List.of(bookPresence));

        String result = mockMvc.perform(get("/api/v1/library/{id}/all", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assert expected.equals(result);
    }

    @Test
    void getAllBooksByLibraryIdWithInvalidData() throws Exception {
        given(libraryService.getAllBooksByLibraryId(anyLong())).willThrow(new EntityNotFoundException("Library"));

        mockMvc.perform(get("/api/v1/library/{id}/all", 0L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteLibrary() throws Exception {
        willDoNothing().given(libraryService).deleteLibrary(anyLong());

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