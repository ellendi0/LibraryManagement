package com.example.librarymanagement.controller;

import com.example.librarymanagement.dto.ErrorDto;
import com.example.librarymanagement.dto.PublisherDto;
import com.example.librarymanagement.dto.mapper.ErrorMapper;
import com.example.librarymanagement.dto.mapper.PublisherMapper;
import com.example.librarymanagement.exception.EntityNotFoundException;
import com.example.librarymanagement.exception.GlobalExceptionHandler;
import com.example.librarymanagement.model.entity.Publisher;
import com.example.librarymanagement.service.PublisherService;
import com.example.librarymanagement.data.TestDataFactory;
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

@WebMvcTest(PublisherController.class)
public class PublisherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PublisherService publisherService;

    @MockBean
    private PublisherMapper publisherMapper;

    @MockBean
    private GlobalExceptionHandler globalExceptionHandler;

    @MockBean
    private ErrorMapper errorMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Publisher publisher1;
    private PublisherDto publisherDto1;
    private ErrorDto errorDto1;
    private ErrorDto errorDto2;

    @BeforeEach
    public void init() {
        publisher1 = TestDataFactory.createPublisher();
        publisherDto1 = new PublisherMapper().toPublisherDto(publisher1);
        
        errorDto1 = new ErrorMapper().toErrorDto(HttpStatus.BAD_REQUEST, "Invalid data");
        errorDto2 = new ErrorMapper().toErrorDto(HttpStatus.NOT_FOUND, "Publisher");
    }

    @Test
    void createPublisher() throws Exception {
        String expected = objectMapper.writeValueAsString(publisherDto1);

        given(publisherService.createPublisher(any(Publisher.class))).willReturn(publisher1);
        given(publisherMapper.toPublisherDto((Publisher) any())).willReturn(publisherDto1);

        String result = mockMvc.perform(post("/api/v1/publisher")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(publisherDto1)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        assertEquals(expected, result);
    }

    @Test
    void createPublisherWithInvalidData() throws Exception {
        publisherDto1.setName("");

        given(publisherService.createPublisher(any(Publisher.class))).willThrow(new IllegalArgumentException());
        given(errorMapper.toErrorDto((HttpStatus) any(), (List<String>) any())).willReturn(errorDto1);
        given(globalExceptionHandler.handleIllegalArgumentException(any(IllegalArgumentException.class))).willReturn(errorDto1);

        mockMvc.perform(post("/api/v1/publisher")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(publisherDto1)))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    void updatePublisher() throws Exception {
        String expected = objectMapper.writeValueAsString(publisherDto1);

        given(publisherService.updatePublisher(anyLong(), any(Publisher.class))).willReturn(publisher1);
        given(publisherMapper.toPublisherDto((Publisher) any())).willReturn(publisherDto1);

        String result = mockMvc.perform(put("/api/v1/publisher/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(publisherDto1)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(expected, result);
    }

    @Test
    void updatePublisherWithInvalidData() throws Exception {
        publisherDto1.setName("");

        given(publisherService.updatePublisher(anyLong(), any(Publisher.class))).willThrow(new IllegalArgumentException());
        given(errorMapper.toErrorDto((HttpStatus) any(), (List<String>) any())).willReturn(errorDto1);

        mockMvc.perform(put("/api/v1/publisher/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(publisherDto1)))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    void getAllPublishers() throws Exception {
        String expected = objectMapper.writeValueAsString(List.of(publisherDto1));

        given(publisherService.getAllPublishers()).willReturn(List.of(publisher1));
        given(publisherMapper.toPublisherDto((List<Publisher>) any())).willReturn(List.of(publisherDto1));

        String result = mockMvc.perform(get("/api/v1/publisher"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(expected, result);
    }

    @Test
    void getPublisherById() throws Exception {
        String expected = objectMapper.writeValueAsString(publisherDto1);

        given(publisherService.getPublisherById(1L)).willReturn(publisher1);
        given(publisherMapper.toPublisherDto((Publisher) any())).willReturn(publisherDto1);

        String result = mockMvc.perform(get("/api/v1/publisher/{id}", 1L))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(expected, result);
    }

    @Test
    void getPublisherByIdWithInvalidId() throws Exception {
        given(publisherService.getPublisherById(1L)).willThrow(new EntityNotFoundException("Publisher"));
        given(errorMapper.toErrorDto((HttpStatus) any(), (List<String>) any())).willReturn(errorDto2);

        mockMvc.perform(get("/api/v1/publisher/{id}", 1L))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();
    }
}
