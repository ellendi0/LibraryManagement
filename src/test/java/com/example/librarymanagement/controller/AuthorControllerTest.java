package com.example.librarymanagement.controller;

import com.example.librarymanagement.data.TestDataFactory;
import com.example.librarymanagement.dto.AuthorDto;
import com.example.librarymanagement.dto.ErrorDto;
import com.example.librarymanagement.dto.mapper.AuthorMapper;
import com.example.librarymanagement.dto.mapper.ErrorMapper;
import com.example.librarymanagement.exception.EntityNotFoundException;
import com.example.librarymanagement.exception.GlobalExceptionHandler;
import com.example.librarymanagement.model.entity.Author;
import com.example.librarymanagement.service.AuthorService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private AuthorMapper authorMapper;

    @MockBean
    private ErrorMapper errorMapper;

    @MockBean
    private GlobalExceptionHandler globalExceptionHandler;

    @Autowired
    private ObjectMapper objectMapper;

    private static Author author1;
    private static AuthorDto authorDto1;
    private static ErrorDto errorDto;

    @BeforeEach
    public void init() {
        AuthorMapper authorMapper1 = new AuthorMapper();
        ErrorMapper errorMapper1 = new ErrorMapper();
        
        author1 = TestDataFactory.createAuthor();

        authorDto1 = authorMapper1.toAuthorDto(author1);
        errorDto = errorMapper1.toErrorDto(HttpStatus.BAD_REQUEST, "Invalid data");
    }

    @Test
    void createAuthor() throws Exception {

        String expected = objectMapper.writeValueAsString(authorDto1);

        given(authorService.createAuthor(any(Author.class))).willReturn(author1);
        given(authorMapper.toAuthorDto((Author) any())).willReturn(authorDto1);

        String result = mockMvc.perform(post("/api/v1/author")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(authorDto1)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        assertEquals(expected, result);
    }

    @Test
    void createAuthorWithInvalidData() throws Exception {
        authorDto1.setLastName("author");

        given(authorService.createAuthor(any(Author.class))).willThrow(new IllegalArgumentException());
        given(errorMapper.toErrorDto(any(), (List<String>) any())).willReturn(errorDto);
        given(globalExceptionHandler.handleIllegalArgumentException(any(IllegalArgumentException.class))).willReturn(errorDto);

        mockMvc.perform(post("/api/v1/author")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(authorDto1)))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    void updateAuthor() throws Exception {
        String expected = objectMapper.writeValueAsString(authorDto1);

        given(authorService.updateAuthor(anyLong(), any(Author.class))).willReturn(author1);
        given(authorMapper.toAuthorDto((Author) any())).willReturn(authorDto1);

        String result = mockMvc.perform(put("/api/v1/author/{id}", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(authorDto1)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(expected, result);
    }

    @Test
    void updateAuthorWithInvalidData() throws Exception {
        authorDto1.setLastName("author");

        given(authorService.updateAuthor(anyLong(), any(Author.class))).willThrow(new IllegalArgumentException());
        given(errorMapper.toErrorDto(any(), (List<String>) any())).willReturn(errorDto);
        given(globalExceptionHandler.handleIllegalArgumentException(any(IllegalArgumentException.class))).willReturn(errorDto);

        mockMvc.perform(put("/api/v1/author/{id}", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(authorDto1)))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    void getAllAuthors() throws Exception {
        String expected = objectMapper.writeValueAsString(List.of(authorDto1));

        given(authorService.getAllAuthors()).willReturn(List.of(author1));
        given(authorMapper.toAuthorDto(List.of(author1))).willReturn(List.of(authorDto1));

        String result = mockMvc.perform(get("/api/v1/author"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(expected, result);
    }

    @Test
    void getAuthorById() throws Exception {
        String expected = objectMapper.writeValueAsString(authorDto1);

        given(authorService.getAuthorById(1L)).willReturn(author1);
        given(authorMapper.toAuthorDto(author1)).willReturn(authorDto1);

        String result = mockMvc.perform(get("/api/v1/author/{id}", 1L))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(expected, result);
    }

    @Test
    void getAuthorByIdWithInvalidId() throws Exception {
        given(authorService.getAuthorById(1L)).willThrow(new EntityNotFoundException("Author"));
        given(errorMapper.toErrorDto(HttpStatus.NOT_FOUND, "Author")).willReturn(errorDto);

        mockMvc.perform(get("/api/v1/author/{id}", 1L))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();
    }
}