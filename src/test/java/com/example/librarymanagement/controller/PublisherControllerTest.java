package com.example.librarymanagement.controller;

import com.example.librarymanagement.dto.PublisherDto;
import com.example.librarymanagement.exception.EntityNotFoundException;
import com.example.librarymanagement.model.entity.Publisher;
import com.example.librarymanagement.service.PublisherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

    @Autowired
    private ObjectMapper objectMapper;

    private static Publisher publisher1;
    private static Publisher publisher2;
    private static PublisherDto publisherDto1;

    @BeforeEach
    public void init() {
        publisher1 = new Publisher();
        publisher1.setId(1L);
        publisher1.setName("Publisher1");

        publisher2 = new Publisher();
        publisher2.setId(2L);
        publisher2.setName("Publisher2");

        publisherDto1 = new PublisherDto(publisher1);
    }

    @Test
    void createPublisher() throws Exception {
        String expected = objectMapper.writeValueAsString(publisherDto1);

        given(publisherService.createPublisher(any(Publisher.class))).willReturn(publisher1);

        String result = mockMvc.perform(post("/api/v1/publisher")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(publisherDto1)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        assertEquals(expected, result);
    }

    @Test
    void createPublisherWithInvalidData() throws Exception {
        publisherDto1.setName("publisher");

        given(publisherService.createPublisher(any(Publisher.class))).willThrow(new IllegalArgumentException());

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

        String result = mockMvc.perform(put("/api/v1/publisher/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(publisherDto1)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(expected, result);
    }

    @Test
    void updatePublisherWithInvalidData() throws Exception {
        publisherDto1.setName("publisher");

        given(publisherService.updatePublisher(anyLong(), any(Publisher.class))).willThrow(new IllegalArgumentException());

        mockMvc.perform(put("/api/v1/publisher/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(publisherDto1)))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    void getAllPublishers() throws Exception {
        String expected = objectMapper.writeValueAsString(List.of(publisherDto1, new PublisherDto(publisher2)));

        given(publisherService.getAllPublishers()).willReturn(List.of(publisher1, publisher2));

        String result = mockMvc.perform(get("/api/v1/publisher/all"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(expected, result);
    }

    @Test
    void getPublisherById() throws Exception {
        String expected = objectMapper.writeValueAsString(publisherDto1);

        given(publisherService.getPublisherById(1L)).willReturn(publisher1);

        String result = mockMvc.perform(get("/api/v1/publisher/{id}", 1L))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(expected, result);
    }

    @Test
    void getPublisherByIdWithInvalidId() throws Exception {
        given(publisherService.getPublisherById(1L)).willThrow(new EntityNotFoundException("Publisher"));

        mockMvc.perform(get("/api/v1/publisher/{id}", 1L))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();
    }
}