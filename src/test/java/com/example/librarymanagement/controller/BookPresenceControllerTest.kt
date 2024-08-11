package com.example.librarymanagement.controller

import com.example.librarymanagement.data.BookPresenceDataFactory
import com.example.librarymanagement.dto.mapper.BookPresenceMapper
import com.example.librarymanagement.dto.mapper.ErrorMapper
import com.example.librarymanagement.dto.mapper.UserMapper
import com.example.librarymanagement.exception.GlobalExceptionHandler
import com.example.librarymanagement.model.entity.BookPresence
import com.example.librarymanagement.service.BookPresenceService
import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class BookPresenceControllerTest {
    private lateinit var mockMvc: MockMvc
    private lateinit var bookPresenceService: BookPresenceService
    private lateinit var bookPresenceController: BookPresenceController
    private lateinit var bookPresenceMapper: BookPresenceMapper
    private lateinit var globalExceptionHandler: GlobalExceptionHandler
    private lateinit var errorMapper: ErrorMapper
    private val objectMapper: ObjectMapper = ObjectMapper()

    private var bookPresence = BookPresenceDataFactory.createBookPresence()
    private var bookPresenceDto = BookPresenceMapper(UserMapper()).toBookPresenceDto(bookPresence)
    private var errorDtoNotFound = ErrorMapper().toErrorDto(HttpStatus.NOT_FOUND, "Book")

    @BeforeEach
    fun setUp() {
        bookPresenceService = mockk(relaxed = true)
        bookPresenceController = mockk(relaxed = true)
        bookPresenceMapper = mockk(relaxed = true)
        errorMapper = mockk(relaxed = true)
        globalExceptionHandler = GlobalExceptionHandler(errorMapper)
        bookPresenceController = BookPresenceController(bookPresenceService, bookPresenceMapper)

        mockMvc = MockMvcBuilders.standaloneSetup(bookPresenceController)
            .setControllerAdvice(globalExceptionHandler)
            .build()
    }

    @Test
    fun shouldAddBookToLibrary() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(bookPresenceDto)

        every { bookPresenceService.addBookToLibrary(any(), any()) } returns bookPresence
        every { bookPresenceMapper.toBookPresenceDto(any<BookPresence>()) } returns bookPresenceDto

        //WHEN
        val actual = mockMvc.perform(post("/api/v1/library/{libraryId}/book/{bookId}/presence", 1L, 1L)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldGetAllBooksByLibraryId() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(listOf(bookPresenceDto))

        every { bookPresenceService.getByLibraryId(any()) } returns listOf(bookPresence)
        every { bookPresenceMapper.toBookPresenceDto(any<List<BookPresence>>()) } returns listOf(bookPresenceDto)

        //WHEN
        val actual = mockMvc.perform(get("/api/v1/library/{libraryId}/book", 1L)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldGetAllBooksByLibraryIdAndBookId() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(listOf(bookPresenceDto))

        every { bookPresenceService.getAllBookByLibraryIdAndBookId(any(), any()) } returns listOf(bookPresence)
        every { bookPresenceMapper.toBookPresenceDto(any<List<BookPresence>>()) } returns listOf(bookPresenceDto)

        //WHEN
        val actual = mockMvc.perform(get("/api/v1/library/{libraryId}/book/{bookId}/presence", 1L, 1L)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldGetAllBooksByLibraryIdAndAvailability1() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(listOf(bookPresenceDto))

        every { bookPresenceService.getAllBookByLibraryIdAndAvailability(any(), any()) } returns listOf(bookPresence)
        every { bookPresenceMapper.toBookPresenceDto(any<List<BookPresence>>()) } returns listOf(bookPresenceDto)

        //WHEN
        val actual = mockMvc.perform(get("/api/v1/library/{libraryId}/book", 1L)
            .param("availability", "AVAILABLE")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldGetAllBooksByLibraryIdAndAvailability2() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(listOf(bookPresenceDto))

        every { bookPresenceService.getAllBookByLibraryIdAndAvailability(any(), any()) } returns listOf(bookPresence)
        every { bookPresenceMapper.toBookPresenceDto(any<List<BookPresence>>()) } returns listOf(bookPresenceDto)

        //WHEN
        val actual = mockMvc.perform(get("/api/v1/library/{libraryId}/book", 1L)
            .param("availability", "UNAVAILABLE")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldRemoveBookFromLibrary() {
        //GIVEN
        val status = 204

        every { bookPresenceService.deleteBookPresenceById(any()) } returns Unit

        //WHEN
        val actual = mockMvc.perform(delete("/api/v1/library/presence/{presenceId}", 1L)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())

        Assertions.assertEquals(status, actual.andReturn().response.status)
    }
}
