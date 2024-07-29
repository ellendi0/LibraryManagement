package com.example.librarymanagement.controller;

import com.example.librarymanagement.data.TestDataFactory;
import com.example.librarymanagement.dto.ErrorDto;
import com.example.librarymanagement.dto.LibraryDto;
import com.example.librarymanagement.dto.mapper.ErrorMapper;
import com.example.librarymanagement.dto.mapper.LibraryMapper;
import com.example.librarymanagement.exception.GlobalExceptionHandler;
import com.example.librarymanagement.model.entity.Library;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
    private GlobalExceptionHandler globalExceptionHandler;

    @Autowired
    private ObjectMapper objectMapper;

    private static LibraryDto libraryDto;
    private static Library library;
    private static ErrorDto errorDto;

    @BeforeEach
    void setUp() {
        LibraryMapper libraryMapper1 = new LibraryMapper();
        ErrorMapper errorMapper1 = new ErrorMapper();

        library = TestDataFactory.createLibrary();

        libraryDto = libraryMapper1.toLibraryDto(library);
        errorDto = errorMapper1.toErrorDto(HttpStatus.BAD_REQUEST, "Invalid data");
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
        given(errorMapper.toErrorDto(any(), (List<String>) any())).willReturn(errorDto);

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
        given(errorMapper.toErrorDto(any(), (List<String>) any())).willReturn(errorDto);

        mockMvc.perform(put("/api/v1/library/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(libraryDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteLibrary() throws Exception {
        willDoNothing().given(libraryService).deleteLibrary(anyLong());
        given(libraryMapper.toLibraryDto((Library) any())).willReturn(libraryDto);

        mockMvc.perform(delete("/api/v1/library/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}