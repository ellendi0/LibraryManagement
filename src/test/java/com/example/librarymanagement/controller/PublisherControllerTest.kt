package com.example.librarymanagement.controller

import com.example.librarymanagement.data.ErrorDataFactory
import com.example.librarymanagement.data.PublisherDataFactory
import com.example.librarymanagement.dto.mapper.ErrorMapper
import com.example.librarymanagement.dto.mapper.PublisherMapper
import com.example.librarymanagement.exception.GlobalExceptionHandler
import com.example.librarymanagement.model.entity.Publisher
import com.example.librarymanagement.service.PublisherService
import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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

    private var publisher = PublisherDataFactory.createPublisher()
    private var publisherDto = PublisherMapper().toPublisherDto(publisher)
    private var errorDtoBadRequest = ErrorDataFactory.createBadRequestError()

    @BeforeEach
    fun setUp() {
        publisherService = mockk(relaxed = true)
        publisherController = mockk(relaxed = true)
        publisherMapper = mockk(relaxed = true)
        errorMapper = mockk(relaxed = true)
        globalExceptionHandler = GlobalExceptionHandler(errorMapper)
        publisherController = PublisherController(publisherService, publisherMapper)

        mockMvc = MockMvcBuilders.standaloneSetup(publisherController)
            .setControllerAdvice(globalExceptionHandler)
            .build()
    }

    @Test
    fun shouldCreatePublisher() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(publisherDto)

        every { publisherService.createPublisher(any()) } returns publisher
        every { publisherMapper.toPublisherDto(any<Publisher>()) } returns publisherDto

        //WHEN
        val actual = mockMvc.perform(post("/api/v1/publisher")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(publisherDto)))
            .andExpect(status().isCreated())
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldNotCreatePublisher() {
        //GIVEN
        val content = objectMapper.writeValueAsString(publisherDto.copy(name = ""))
        val expected = objectMapper.writeValueAsString(errorDtoBadRequest)

        every { publisherService.createPublisher(any()) } throws (IllegalArgumentException("Invalid"))
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDtoBadRequest

        //WHEN
        val actual = mockMvc.perform(post("/api/v1/publisher")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isBadRequest())
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldUpdatePublisher() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(publisherDto)

        every { publisherService.updatePublisher(any()) } returns publisher
        every { publisherMapper.toPublisherDto(any<Publisher>()) } returns publisherDto

        //WHEN
        val actual = mockMvc.perform(put("/api/v1/publisher/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(expected))
            .andExpect(status().isOk())
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldNotUpdatePublisher() {
        //GIVEN
        val content = objectMapper.writeValueAsString(publisherDto.copy(name = ""))
        val expected = objectMapper.writeValueAsString(errorDtoBadRequest)

        every { publisherService.updatePublisher(any()) } throws IllegalArgumentException("Invalid")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDtoBadRequest

        //WHEN
        val actual = mockMvc.perform(put("/api/v1/publisher/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isBadRequest())
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldNotUpdatePublisherWithInvalidData() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(errorDtoBadRequest)
        val content = objectMapper.writeValueAsString(publisherDto.copy(name = ""))

        every { publisherService.updatePublisher(any()) } throws IllegalArgumentException("Invalid")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDtoBadRequest

        //WHEN
        val actual = mockMvc.perform(put("/api/v1/publisher/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isBadRequest())
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldGetPublisherById() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(publisherDto)

        every { publisherService.getPublisherById(any()) } returns publisher
        every { publisherMapper.toPublisherDto(any<Publisher>()) } returns publisherDto

        //WHEN
        val actual = mockMvc.perform(get("/api/v1/publisher/{id}", 1L))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldGetAllPublishers() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(listOf(publisherDto))

        every { publisherService.getAllPublishers() } returns listOf(publisher)
        every { publisherMapper.toPublisherDto(any<List<Publisher>>()) } returns listOf(publisherDto)

        val actual = mockMvc.perform(get("/api/v1/publisher"))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        Assertions.assertEquals(expected, actual)
    }
}
