package com.example.librarymanagement.controller

import com.example.librarymanagement.data.TestDataFactory
import com.example.librarymanagement.dto.BookPresenceDto
import com.example.librarymanagement.dto.mapper.BookPresenceMapper
import com.example.librarymanagement.dto.mapper.ErrorMapper
import com.example.librarymanagement.dto.mapper.UserMapper
import com.example.librarymanagement.exception.EntityNotFoundException
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

    private var bookPresence = TestDataFactory.createBookPresence()
    private lateinit var bookPresenceDto: BookPresenceDto
    private var errorDto1 = ErrorMapper().toErrorDto(HttpStatus.BAD_REQUEST, "Invalid")
    private var errorDto2 = ErrorMapper().toErrorDto(HttpStatus.NOT_FOUND, "Book")

    @BeforeEach
    fun setUp() {
        bookPresenceService = mockk(relaxed = true)
        bookPresenceController = mockk(relaxed = true)
        bookPresenceMapper = mockk(relaxed = true)
        errorMapper = mockk(relaxed = true)
        globalExceptionHandler = GlobalExceptionHandler(errorMapper)
        bookPresenceController = BookPresenceController(bookPresenceService, bookPresenceMapper)

        bookPresenceDto = BookPresenceMapper(UserMapper()).toBookPresenceDto(bookPresence)

        mockMvc = MockMvcBuilders.standaloneSetup(bookPresenceController)
            .setControllerAdvice(globalExceptionHandler)
            .build()
    }

    @Test
    fun shouldAddBookToLibrary() {
        val expected = objectMapper.writeValueAsString(bookPresenceDto)

        every { bookPresenceService.addBookToLibrary(any(), any()) } returns bookPresence
        every { bookPresenceMapper.toBookPresenceDto(any<BookPresence>()) } returns bookPresenceDto

        val result = mockMvc.perform(post("/api/v1/library/{libraryId}/book/{bookId}/presence", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andReturn().response.contentAsString

        Assertions.assertEquals(expected, result)
    }

    @Test
    fun shouldNotAddBookToLibraryWithInvalidData() {
        every { bookPresenceService.addBookToLibrary(any(), any()) } throws (EntityNotFoundException("Book"))
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto2

        mockMvc.perform(post("/api/v1/library/{libraryId}/book/{bookId}/presence", 0L, 0L)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
    }

    @Test
    fun shouldGetAllBooksByLibraryId() {
        val expected = objectMapper.writeValueAsString(listOf(bookPresenceDto))

        every { bookPresenceService.getByLibraryId(any()) } returns listOf(bookPresence)
        every { bookPresenceMapper.toBookPresenceDto(any<List<BookPresence>>()) } returns listOf(bookPresenceDto)

        val result = mockMvc.perform(get("/api/v1/library/{libraryId}/book", 1L)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn().response.contentAsString

        Assertions.assertEquals(expected, result)
    }

    @Test
    fun shouldNotGetAllBooksByLibraryIdWithInvalidData() {
        every { bookPresenceService.getByLibraryId(any()) } throws (EntityNotFoundException("Library"))
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto2

        mockMvc.perform(get("/api/v1/library/{libraryId}/book", 0L)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
    }

    @Test
    fun shouldGetAllBooksByLibraryIdAndBookId() {
        val expected = objectMapper.writeValueAsString(listOf(bookPresenceDto))

        every { bookPresenceService.getAllBookByLibraryIdAndBookId(any(), any()) } returns listOf(bookPresence)
        every { bookPresenceMapper.toBookPresenceDto(any<List<BookPresence>>()) } returns listOf(bookPresenceDto)

        val result = mockMvc.perform(get("/api/v1/library/{libraryId}/book/{bookId}/presence", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn().response.contentAsString

        Assertions.assertEquals(expected, result)
    }

    @Test
    fun shouldNotGetAllBooksByLibraryIdAndBookIdWithInvalidData() {
        every { bookPresenceService.getAllBookByLibraryIdAndBookId(any(), any()) } throws (EntityNotFoundException("Book"))
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto2

        mockMvc.perform(get("/api/v1/library/{libraryId}/book/{bookId}/presence", 0L, 0L)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
    }

    @Test
    fun shouldGetAllBooksByLibraryIdAndAvailability1() {
        val expected = objectMapper.writeValueAsString(listOf(bookPresenceDto))

        every { bookPresenceService.getAllBookByLibraryIdAndAvailability(any(), any()) } returns listOf(bookPresence)
        every { bookPresenceMapper.toBookPresenceDto(any<List<BookPresence>>()) } returns listOf(bookPresenceDto)

        val result = mockMvc.perform(get("/api/v1/library/{libraryId}/book", 1L)
                .param("availability", "AVAILABLE")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn().response.contentAsString

        Assertions.assertEquals(expected, result)
    }

    @Test
    fun shouldGetAllBooksByLibraryIdAndAvailability2() {
        val expected = objectMapper.writeValueAsString(listOf(bookPresenceDto))

        every { bookPresenceService.getAllBookByLibraryIdAndAvailability(any(), any()) } returns listOf(bookPresence)
        every { bookPresenceMapper.toBookPresenceDto(any<List<BookPresence>>()) } returns listOf(bookPresenceDto)

        val result = mockMvc.perform(get("/api/v1/library/{libraryId}/book", 1L)
                .param("availability", "UNAVAILABLE")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn().response.contentAsString

        Assertions.assertEquals(expected, result)
    }

    @Test
    fun shouldNotGetAllBooksByLibraryIdAndStatusWithInvalidData() {
        every { bookPresenceService.getAllBookByLibraryIdAndAvailability(any(), any()) } throws (EntityNotFoundException("Library"))
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto2

        mockMvc.perform(get("/api/v1/library/{libraryId}/book", 0L)
                .param("availability", "AVAILABLE")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
    }

    @Test
    fun shouldNotGetAllBooksByLibraryIdAndStatusWithInvalidStatus() {
        every { bookPresenceService.getAllBookByLibraryIdAndAvailability(any(), any()) } throws (EntityNotFoundException("Library"))
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto2

        mockMvc.perform(get("/api/v1/library/{libraryId}/book", 0L)
                .param("availability", "INVALID")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun shouldRemoveBookFromLibrary() {
        every { bookPresenceService.deleteBookPresenceById(any()) } returns Unit

        mockMvc.perform(delete("/api/v1/presence/{presenceId}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
    }
}