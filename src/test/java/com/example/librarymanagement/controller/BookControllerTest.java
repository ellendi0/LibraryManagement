package com.example.librarymanagement.controller;

import com.example.librarymanagement.data.TestDataFactory;
import com.example.librarymanagement.dto.BookRequestDto;
import com.example.librarymanagement.dto.BookResponseDto;
import com.example.librarymanagement.dto.ErrorDto;
import com.example.librarymanagement.dto.mapper.BookMapper;
import com.example.librarymanagement.dto.mapper.ErrorMapper;
import com.example.librarymanagement.exception.EntityNotFoundException;
import com.example.librarymanagement.exception.GlobalExceptionHandler;
import com.example.librarymanagement.model.entity.Book;
import com.example.librarymanagement.service.BookService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private BookMapper bookMapper;

    @MockBean
    private ErrorMapper errorMapper;

    @MockBean
    private GlobalExceptionHandler globalExceptionHandler;

    @Autowired
    private ObjectMapper objectMapper;

    private static BookRequestDto bookRequestDto;
    private static BookRequestDto bookUpdateRequestDto;
    private static BookResponseDto bookResponseDto;
    private static Book book1;
    private static Book updatedBook;
    private static ErrorDto errorDto1;
    private static ErrorDto errorDto2;
    private static BookMapper bookMapper1;
    private static ErrorMapper errorMapper1;

    @BeforeEach
    void setUp() {
        TestDataFactory.TestDataRel testData = TestDataFactory.createTestDataRel();
        bookMapper1 = new BookMapper();
        errorMapper1 = new ErrorMapper();

        book1 = testData.book;
        updatedBook = testData.book;
        updatedBook.setTitle("Book Updated");

        bookRequestDto = TestDataFactory.createBookRequestDto();
        bookUpdateRequestDto = TestDataFactory.createBookRequestDto();
        bookUpdateRequestDto.setTitle("Book Updated");

        bookResponseDto = bookMapper1.toBookDto(book1);
        errorDto1 = errorMapper1.toErrorDto(HttpStatus.BAD_REQUEST, "Invalid data");
        errorDto2 = errorMapper1.toErrorDto(HttpStatus.NOT_FOUND, "Book not found");
    }

    @Test
    void createBook() throws Exception {
        String expected = objectMapper.writeValueAsString(bookResponseDto);

        given(bookService.createBook(anyLong(), anyLong(), any(Book.class))).willReturn(book1);
        given(bookMapper.toBookDto((Book) any())).willReturn(bookResponseDto);

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

        given(errorMapper.toErrorDto((HttpStatus) any(), (List<String>) any())).willReturn(errorDto1);

        mockMvc.perform(post("/api/v1/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createBookWithEmpty() throws Exception {
        bookRequestDto.setTitle("");

        given(errorMapper.toErrorDto((HttpStatus) any(), (List<String>) any())).willReturn(errorDto1);

        mockMvc.perform(post("/api/v1/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateBook() throws Exception {
        BookResponseDto updatedBookResponseDto = bookMapper1.toBookDto(updatedBook);

        String expected = objectMapper.writeValueAsString(updatedBookResponseDto);

        given(bookService.updateBook(anyLong(), any(Book.class))).willReturn(updatedBook);
        given(bookMapper.toBookDto((Book) any())).willReturn(updatedBookResponseDto);

        String result = mockMvc.perform(put("/api/v1/book/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookUpdateRequestDto)))
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
        given(bookMapper.toBookDto((Book) any())).willReturn(bookResponseDto);

        String result = mockMvc.perform(get("/api/v1/book/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(expected, result);
    }

    @Test
    void getBookByNonExistingId() throws Exception {
        given(bookService.getBookById(anyLong())).willThrow(new EntityNotFoundException("Book"));

        given(errorMapper.toErrorDto((HttpStatus) any(), (List<String>) any())).willReturn(errorDto2);
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
        given(bookMapper.toBookDto(books)).willReturn(bookResponseDtos);

        String result = mockMvc.perform(get("/api/v1/book")
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
