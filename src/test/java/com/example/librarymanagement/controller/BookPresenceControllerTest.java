package com.example.librarymanagement.controller;

import com.example.librarymanagement.data.TestDataFactory;
import com.example.librarymanagement.dto.BookPresenceDto;
import com.example.librarymanagement.dto.ErrorDto;
import com.example.librarymanagement.dto.mapper.BookPresenceMapper;
import com.example.librarymanagement.dto.mapper.ErrorMapper;
import com.example.librarymanagement.dto.mapper.LibraryMapper;
import com.example.librarymanagement.dto.mapper.UserMapper;
import com.example.librarymanagement.exception.EntityNotFoundException;
import com.example.librarymanagement.exception.GlobalExceptionHandler;
import com.example.librarymanagement.model.entity.BookPresence;
import com.example.librarymanagement.model.enums.Availability;
import com.example.librarymanagement.service.BookPresenceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookPresenceController.class)
public class BookPresenceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookPresenceService bookPresenceService;

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
    private static BookPresence bookPresence;
    private static ErrorDto errorDto1;


    @BeforeEach
    void setUp() {
        BookPresenceMapper bookPresenceMapper1 = new BookPresenceMapper(new UserMapper());
        ErrorMapper errorMapper1 = new ErrorMapper();

        bookPresence = TestDataFactory.createBookPresence();

        bookPresenceDto = bookPresenceMapper1.toBookPresenceDto(bookPresence);
        errorDto1 = errorMapper1.toErrorDto(HttpStatus.NOT_FOUND, "Library");
    }

    @Test
    void addBookToLibrary() throws Exception {
        String expected = objectMapper.writeValueAsString(bookPresenceDto);

        given(bookPresenceService.addBookToLibrary(anyLong(), anyLong())).willReturn(bookPresence);
        given(bookPresenceMapper.toBookPresenceDto((BookPresence) any())).willReturn(bookPresenceDto);

        String result = mockMvc.perform(post("/api/v1/library/{libraryId}/book/{bookId}/presence", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        assertEquals(expected, result);
    }

    @Test
    void addBookToLibraryWithInvalidData() throws Exception {
        given(bookPresenceService.addBookToLibrary(anyLong(), anyLong())).willThrow(new EntityNotFoundException("Book"));
        given(errorMapper.toErrorDto(any(), (List<String>) any())).willReturn(errorDto1);

        mockMvc.perform(post("/api/v1/library/{libraryId}/book/{bookId}/presence", 1L, 1L)
                        .param("bookId", "0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllBooksByLibraryIdAndStatus() throws Exception {
        String expected = objectMapper.writeValueAsString(List.of(bookPresenceDto));

        given(bookPresenceService.getAllBookByLibraryIdAndAvailability(anyLong(), any(Availability.class)))
                .willReturn(List.of(bookPresence));
        given(bookPresenceMapper.toBookPresenceDto((List<BookPresence>) any())).willReturn(List.of(bookPresenceDto));

        String result = mockMvc.perform(get("/api/v1/library/{libraryId}/book", 1L)
                        .param("availability", "AVAILABLE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(expected, result);

    }

    @Test
    void getAllBooksByLibraryIdAndStatusWithInvalidDataId() throws Exception {
        given(bookPresenceService.getAllBookByLibraryIdAndAvailability(anyLong(), any(Availability.class)))
                .willThrow(new EntityNotFoundException("Library"));
        given(errorMapper.toErrorDto(any(), (List<String>) any())).willReturn(errorDto1);

        mockMvc.perform(get("/api/v1/library/{libraryId}/book", 0L)
                        .param("availability", "AVAILABLE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllBooksByLibraryIdAndStatusWithInvalidStatus() throws Exception {
        given(bookPresenceService.getAllBookByLibraryIdAndAvailability(anyLong(), any(Availability.class)))
                .willThrow(new IllegalArgumentException());
        given(errorMapper.toErrorDto(any(), (List<String>) any())).willReturn(errorDto1);

        mockMvc.perform(get("/api/v1/library/{libraryId}/book", 0L)
                        .param("availability", "AVAIE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllBooksByLibraryId() throws Exception {
        String expected = objectMapper.writeValueAsString(List.of(bookPresenceDto));

        given(bookPresenceService.getByLibraryId(anyLong())).willReturn(List.of(bookPresence));
        given(bookPresenceMapper.toBookPresenceDto((List<BookPresence>) any())).willReturn(List.of(bookPresenceDto));

        String result = mockMvc.perform(get("/api/v1/library/{libraryId}/book", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(expected, result);
    }

    @Test
    void getAllBooksByLibraryIdWithInvalidData() throws Exception {
        given(bookPresenceService.getByLibraryId(anyLong())).willThrow(new EntityNotFoundException("Library"));
        given(errorMapper.toErrorDto(any(), (List<String>) any())).willReturn(errorDto1);

        mockMvc.perform(get("/api/v1/library/{libraryId}/book", 0L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void removeBookFromLibrary() throws Exception {
        willDoNothing().given(bookPresenceService).deleteBookPresenceById(anyLong());

        mockMvc.perform(delete("/api/v1/library/presence/{presenceId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
