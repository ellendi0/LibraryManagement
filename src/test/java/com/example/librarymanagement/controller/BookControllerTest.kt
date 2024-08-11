package com.example.librarymanagement.controller

import com.example.librarymanagement.data.BookDataFactory
import com.example.librarymanagement.data.ErrorDataFactory
import com.example.librarymanagement.dto.mapper.BookMapper
import com.example.librarymanagement.dto.mapper.ErrorMapper
import com.example.librarymanagement.exception.GlobalExceptionHandler
import com.example.librarymanagement.model.entity.Book
import com.example.librarymanagement.service.BookService
import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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

    private var book = BookDataFactory.createBook()
    private var bookRequestDto = BookDataFactory.createBookRequestDto()
    private var bookResponseDto = BookMapper().toBookDto(book)
    private var errorDtoBadRequest = ErrorDataFactory.createBadRequestError()

    @BeforeEach
    fun setUp() {
        bookService = mockk(relaxed = true)
        bookController = mockk(relaxed = true)
        bookMapper = mockk(relaxed = true)
        errorMapper = mockk(relaxed = true)
        globalExceptionHandler = GlobalExceptionHandler(errorMapper)
        bookController = BookController(bookService, bookMapper)

        mockMvc = MockMvcBuilders.standaloneSetup(bookController)
            .setControllerAdvice(globalExceptionHandler)
            .build()
    }

    @Test
    fun shouldCreateBook() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(bookResponseDto)
        val content = objectMapper.writeValueAsString(bookRequestDto)

        every { bookService.createBook(any(), any(), any()) } returns book
        every { bookMapper.toBookDto(any<Book>()) } returns bookResponseDto

        //WHEN
        val actual = mockMvc.perform(post("/api/v1/book")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isCreated())
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldNotCreateBookWithInvalidData() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(errorDtoBadRequest)
        val content = objectMapper.writeValueAsString(bookRequestDto.copy(title = ""))

        every { bookService.createBook(any(), any(), any()) } throws IllegalArgumentException("Invalid")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDtoBadRequest

        //WHEN
        val actual = mockMvc.perform(post("/api/v1/book")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isBadRequest())
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldNotCreateBookWithInvalidData2() {
        //GIVEN
        val content = objectMapper.writeValueAsString(bookRequestDto.copy(title = ""))
        val expected = objectMapper.writeValueAsString(errorDtoBadRequest)

        every { bookService.createBook(any(), any(), any()) } throws (IllegalArgumentException("Invalid"))
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDtoBadRequest

        //WHEN
        val actual = mockMvc.perform(post("/api/v1/book")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isBadRequest())
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldUpdateBook() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(bookResponseDto)

        every { bookService.updateBook(any()) } returns book
        every { bookMapper.toBookDto(any<Book>()) } returns bookResponseDto

        //WHEN
        val actual = mockMvc.perform(put("/api/v1/book/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(expected))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldNotUpdateBookWithInvalidData() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(errorDtoBadRequest)
        val content = objectMapper.writeValueAsString(bookRequestDto.copy(title = ""))

        every { bookService.updateBook(any()) } throws (IllegalArgumentException("Invalid"))
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDtoBadRequest

        //WHEN
        val actual = mockMvc.perform(put("/api/v1/book/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isBadRequest())
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldGetBookById() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(bookResponseDto)

        every { bookService.getBookById(any()) } returns book
        every { bookMapper.toBookDto(any<Book>()) } returns bookResponseDto

        //WHEN
        val actual = mockMvc.perform(get("/api/v1/book/{id}", 1L))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldGetAllBooks() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(listOf(bookResponseDto))

        every { bookService.findAll() } returns listOf(book)
        every { bookMapper.toBookDto(any<List<Book>>()) } returns listOf(bookResponseDto)

        //WHEN
        val actual = mockMvc.perform(get("/api/v1/book"))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldDeleteBook() {
        every { bookService.deleteBook(any()) } returns Unit

        mockMvc.perform(delete("/api/v1/book/{id}", 1L))
            .andExpect(status().isNoContent)
    }

    @Test
    fun shouldGetBookByTitleAndAuthor() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(bookResponseDto)

        every { bookService.getBookByTitleAndAuthor(any(), any()) } returns book
        every { bookMapper.toBookDto(any<Book>()) } returns bookResponseDto

        //WHEN
        val actual = mockMvc.perform(get("/api/v1/book?title=Title&author=1"))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }
}
