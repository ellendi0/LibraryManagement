package com.example.librarymanagement.controller

import com.example.librarymanagement.data.TestDataFactory
import com.example.librarymanagement.dto.BookRequestDto
import com.example.librarymanagement.dto.mapper.BookMapper
import com.example.librarymanagement.dto.mapper.ErrorMapper
import com.example.librarymanagement.exception.EntityNotFoundException
import com.example.librarymanagement.exception.GlobalExceptionHandler
import com.example.librarymanagement.model.entity.Book
import com.example.librarymanagement.service.BookService
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class BookControllerTest {
    private lateinit var mockMvc: MockMvc
    private lateinit var bookService: BookService
    private lateinit var bookController: BookController
    private lateinit var bookMapper: BookMapper
    private lateinit var globalExceptionHandler: GlobalExceptionHandler
    private lateinit var errorMapper: ErrorMapper
    private val objectMapper: ObjectMapper = ObjectMapper()

    private var book = TestDataFactory.createBook()
    private lateinit var bookRequestDto: BookRequestDto
    private var bookResponseDto = BookMapper().toBookDto(book)
    private var errorDto1 = ErrorMapper().toErrorDto(HttpStatus.BAD_REQUEST, "Invalid")
    private var errorDto2 = ErrorMapper().toErrorDto(HttpStatus.NOT_FOUND, "Not found")

    @BeforeEach
    fun setUp() {
        bookService = mockk(relaxed = true)
        bookController = mockk(relaxed = true)
        bookMapper = mockk(relaxed = true)
        errorMapper = mockk(relaxed = true)
        globalExceptionHandler = GlobalExceptionHandler(errorMapper)
        bookController = BookController(bookService, bookMapper)

        bookRequestDto = TestDataFactory.createBookRequestDto()

        mockMvc = MockMvcBuilders.standaloneSetup(bookController)
            .setControllerAdvice(globalExceptionHandler)
            .build()
    }

    @Test
    fun shouldCreateBook() {
        val expected = objectMapper.writeValueAsString(bookResponseDto)

        every { bookService.createBook(any(), any(), any()) } returns book
        every { bookMapper.toBookDto(any<Book>()) } returns bookResponseDto

        val result = mockMvc.perform(post("/api/v1/book")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(bookRequestDto)))
            .andExpect(status().isCreated())
            .andReturn()

        Assertions.assertEquals(expected, result.response.contentAsString)
    }

    @Test
    fun shouldNotCreateBookWithInvalidData() {
        bookRequestDto.title = ""

        every { bookService.createBook(any(), any(), any()) } throws (IllegalArgumentException("Invalid"))
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto1

        mockMvc.perform(post("/api/v1/book")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(bookRequestDto)))
            .andExpect(status().isBadRequest())
    }

    @Test
    fun shouldNotCreateBookWithInvalidData2() {
        bookRequestDto.authorId = 0

        every { bookService.createBook(any(), any(), any()) } throws (IllegalArgumentException("Invalid"))
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto1

        val result = mockMvc.perform(post("/api/v1/book")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(bookRequestDto)))
            .andExpect(status().isBadRequest())
    }

    @Test
    fun shouldUpdateBook() {
        val expected = objectMapper.writeValueAsString(bookResponseDto)

        every { bookService.updateBook(any(), any()) } returns book
        every { bookMapper.toBookDto(any<Book>()) } returns bookResponseDto

        val result = mockMvc.perform(put("/api/v1/book/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(bookRequestDto)))
            .andExpect(status().isOk)
            .andReturn()

        Assertions.assertEquals(expected, result.response.contentAsString)
    }

    @Test
    fun shouldNotUpdateBookWithInvalidData() {
        bookRequestDto.title = ""

        every { bookService.updateBook(any(), any()) } throws (IllegalArgumentException("Invalid"))
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto1

        mockMvc.perform(put("/api/v1/book/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(bookRequestDto)))
            .andExpect(status().isBadRequest())
    }

    @Test
    fun shouldGetBookById() {
        val expected = objectMapper.writeValueAsString(bookResponseDto)

        every { bookService.getBookById(any()) } returns book
        every { bookMapper.toBookDto(any<Book>()) } returns bookResponseDto

        val result = mockMvc.perform(get("/api/v1/book/{id}", 1L))
            .andExpect(status().isOk)
            .andReturn()

        Assertions.assertEquals(expected, result.response.contentAsString)
    }

    @Test
    fun shouldNotGetBookByNotExistingId() {
        every { bookService.getBookById(any()) } throws (EntityNotFoundException("Not found"))
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto2

        mockMvc.perform(get("/api/v1/book/{id}", 1L))
            .andExpect(status().isNotFound())
    }

    @Test
    fun shouldGetAllBooks() {
        val expected = objectMapper.writeValueAsString(listOf(bookResponseDto))

        every { bookService.findAll() } returns listOf(book)
        every { bookMapper.toBookDto(any<List<Book>>()) } returns listOf(bookResponseDto)

        val result = mockMvc.perform(get("/api/v1/book"))
            .andExpect(status().isOk)
            .andReturn()

        Assertions.assertEquals(expected, result.response.contentAsString)
    }

    @Test
    fun shouldDeleteBook() {
        every { bookService.deleteBook(any()) } returns Unit

        mockMvc.perform(delete("/api/v1/book/{id}", 1L))
            .andExpect(status().isNoContent)
    }

    @Test
    fun shouldGetBookByTitleAndAuthor() {
        val expected = objectMapper.writeValueAsString(bookResponseDto)

        every { bookService.getBookByTitleAndAuthor(any(), any()) } returns book
        every { bookMapper.toBookDto(any<Book>()) } returns bookResponseDto

        val result = mockMvc.perform(get("/api/v1/book?title=Title&author=1"))
            .andExpect(status().isOk)
            .andReturn()

        Assertions.assertEquals(expected, result.response.contentAsString)
    }

    @Test
    fun shouldNotGetBookByTitleAndAuthor1() {
        every { bookService.getBookByTitleAndAuthor(any(), any()) } throws (EntityNotFoundException("Not found"))
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto2

        mockMvc.perform(get("/api/v1/book")
            .param("title", "0")
            .param("author", "0"))
            .andExpect(status().isNotFound())
    }

    @Test
    fun shouldNotGetBookByTitleAndAuthor2() {
        every { bookService.getBookByTitleAndAuthor(any(), any()) } throws (EntityNotFoundException("Not found"))
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto2

        mockMvc.perform(get("/api/v1/book")
            .param("title", "")
            .param("author", "0"))
            .andExpect(status().isNotFound())
    }
}