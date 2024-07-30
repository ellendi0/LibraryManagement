package com.example.librarymanagement.controller

import com.example.librarymanagement.data.TestDataFactory
import com.example.librarymanagement.dto.PublisherDto
import com.example.librarymanagement.dto.mapper.ErrorMapper
import com.example.librarymanagement.dto.mapper.PublisherMapper
import com.example.librarymanagement.exception.EntityNotFoundException
import com.example.librarymanagement.exception.GlobalExceptionHandler
import com.example.librarymanagement.model.entity.Publisher
import com.example.librarymanagement.service.PublisherService
import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class PublisherControllerTest {
    private lateinit var mockMvc: MockMvc
    private lateinit var publisherService: PublisherService
    private lateinit var publisherController: PublisherController
    private lateinit var publisherMapper: PublisherMapper
    private lateinit var globalExceptionHandler: GlobalExceptionHandler
    private lateinit var errorMapper: ErrorMapper
    private val objectMapper: ObjectMapper = ObjectMapper()

    private var publisher = TestDataFactory.createPublisher()
    private lateinit var publisherDto: PublisherDto
    private var errorDto1 = ErrorMapper().toErrorDto(HttpStatus.BAD_REQUEST, "Invalid")
    private var errorDto2 = ErrorMapper().toErrorDto(HttpStatus.NOT_FOUND, "Publisher")

    @BeforeEach
    fun setUp() {
        publisherService = mockk(relaxed = true)
        publisherController = mockk(relaxed = true)
        publisherMapper = mockk(relaxed = true)
        errorMapper = mockk(relaxed = true)
        globalExceptionHandler = GlobalExceptionHandler(errorMapper)
        publisherController = PublisherController(publisherService, publisherMapper)

        publisherDto = PublisherMapper().toPublisherDto(publisher)

        mockMvc = MockMvcBuilders.standaloneSetup(publisherController)
            .setControllerAdvice(globalExceptionHandler)
            .build()
    }

    @Test
    fun shouldCreatePublisher() {
        val expected = objectMapper.writeValueAsString(publisherDto)

        every { publisherService.createPublisher(any()) } returns publisher
        every { publisherMapper.toPublisherDto(any<Publisher>()) } returns publisherDto

        val result = mockMvc.perform(post("/api/v1/publisher")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(publisherDto)))
            .andExpect(status().isCreated())
            .andReturn()
    }

    @Test
    fun shouldNotCreatePublisher() {
        every { publisherService.createPublisher(any()) } throws IllegalArgumentException("Invalid")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto1

        mockMvc.perform(post("/api/v1/publisher")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(publisherDto)))
            .andExpect(status().isBadRequest())
            .andReturn()
    }

    @Test
    fun shouldUpdatePublisher() {
        val expected = objectMapper.writeValueAsString(publisherDto)

        every { publisherService.updatePublisher(any(), any()) } returns publisher
        every { publisherMapper.toPublisherDto(any<Publisher>()) } returns publisherDto

        val result = mockMvc.perform(put("/api/v1/publisher/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(publisherDto)))
            .andExpect(status().isOk())
            .andReturn().response.contentAsString

        Assertions.assertEquals(expected, result)
    }

    @Test
    fun shouldNotUpdatePublisher() {
        every { publisherService.updatePublisher(any(), any()) } throws IllegalArgumentException("Invalid")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto1

        mockMvc.perform(put("/api/v1/publisher/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(publisherDto)))
            .andExpect(status().isBadRequest())
            .andReturn()
    }

    @Test
    fun shouldNotUpdateNonExistingPublisher() {
        every { publisherService.updatePublisher(any(), any()) } throws EntityNotFoundException("Publisher")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto2

        mockMvc.perform(put("/api/v1/publisher/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(publisherDto)))
            .andExpect(status().isNotFound())
            .andReturn()
    }

    @Test
    fun shouldNotUpdatePublisherWithInvalidData() {
        publisherDto.name = ""

        every { publisherService.updatePublisher(any(), any()) } throws IllegalArgumentException("Invalid")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto1

        mockMvc.perform(put("/api/v1/publisher/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(publisherDto)))
            .andExpect(status().isBadRequest())
            .andReturn()
    }

    @Test
    fun shouldGetPublisherById() {
        val expected = objectMapper.writeValueAsString(publisherDto)

        every { publisherService.getPublisherById(any()) } returns publisher
        every { publisherMapper.toPublisherDto(any<Publisher>()) } returns publisherDto

        val result = mockMvc.perform(get("/api/v1/publisher/{id}", 1L))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        Assertions.assertEquals(expected, result)
    }

    @Test
    fun shouldNotGetPublisherById() {
        every { publisherService.getPublisherById(any()) } throws EntityNotFoundException("Publisher")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto2

        mockMvc.perform(get("/api/v1/publisher/{id}", 0L))
            .andExpect(status().isNotFound)
            .andReturn()
    }

    @Test
    fun shouldGetAllPublishers() {
        val expected = objectMapper.writeValueAsString(listOf(publisherDto))

        every { publisherService.getAllPublishers() } returns listOf(publisher)
        every { publisherMapper.toPublisherDto(any<List<Publisher>>()) } returns listOf(publisherDto)

        val result = mockMvc.perform(get("/api/v1/publisher"))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        Assertions.assertEquals(expected, result)
    }
}