package com.example.librarymanagement.controller

import com.example.librarymanagement.data.BookDataFactory
import com.example.librarymanagement.data.ErrorDataFactory
import com.example.librarymanagement.dto.mapper.BookMapper
import com.example.librarymanagement.dto.mapper.ErrorMapper
import com.example.librarymanagement.exception.GlobalExceptionHandler
import com.example.librarymanagement.model.domain.Book
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
    private val bookService: BookService = mockk()
    private val bookMapper: BookMapper = mockk(relaxed = true)
    private val errorMapper: ErrorMapper = mockk()
    private val globalExceptionHandler = GlobalExceptionHandler(errorMapper)
    private val objectMapper: ObjectMapper = ObjectMapper()
    private val bookController = BookController(bookService, bookMapper)

    private var book = BookDataFactory.createBook(ID)
    private var bookRequestDto = BookDataFactory.createBookRequestDto(ID)
    private var bookResponseDto = BookMapper().toBookDto(book)
    private var errorDtoBadRequest = ErrorDataFactory.createBadRequestError()

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController)
            .setControllerAdvice(globalExceptionHandler)
            .build()
    }

    @Test
    fun shouldCreateBook() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(bookResponseDto)
        val content = objectMapper.writeValueAsString(bookRequestDto)

        every { bookService.createBook(any()) } returns book
        every { bookMapper.toBookDto(any<Book>()) } returns bookResponseDto

        //WHEN
        val actual = mockMvc.perform(post(URL)
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

        every { bookService.createBook(any()) } throws IllegalArgumentException("Invalid")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDtoBadRequest

        //WHEN
        val actual = mockMvc.perform(post(URL)
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

        every { bookService.createBook(any()) } throws (IllegalArgumentException("Invalid"))
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDtoBadRequest

        //WHEN
        val actual = mockMvc.perform(post(URL)
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
        val content = objectMapper.writeValueAsString(bookRequestDto)

        every { bookService.updateBook(any()) } returns book
        every { bookMapper.toBookDto(any<Book>()) } returns bookResponseDto

        //WHEN
        val actual = mockMvc.perform(put("$URL/{ID}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
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
        val actual = mockMvc.perform(put("$URL/{ID}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isBadRequest())
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldGetBookByID() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(bookResponseDto)

        every { bookService.getBookById(any()) } returns book
        every { bookMapper.toBookDto(any<Book>()) } returns bookResponseDto

        //WHEN
        val actual = mockMvc.perform(get("$URL/{ID}", 1L))
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
        val actual = mockMvc.perform(get(URL))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldDeleteBook() {
        every { bookService.deleteBookById(any()) } returns Unit

        mockMvc.perform(delete("$URL/{ID}", 1L))
            .andExpect(status().isNoContent)
    }

    @Test
    fun shouldGetBookByTitleAndAuthor() {
        // GIVEN
        val expected = objectMapper.writeValueAsString(bookResponseDto)

        every { bookService.getBookByTitleAndAuthor("Title", ID) } returns book
        every { bookMapper.toBookDto(any<Book>()) } returns bookResponseDto

        // WHEN
        val actual = mockMvc.perform(get(URL)
            .param("title", "Title")
            .param("author", ID))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        // THEN
        Assertions.assertEquals(expected, actual)
    }

    companion object{
        const val ID = BookDataFactory.JPA_ID.toString()
        const val URL = "/api/v1/book"
    }
}
