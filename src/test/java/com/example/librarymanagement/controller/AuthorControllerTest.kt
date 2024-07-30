package com.example.librarymanagement.controller

import com.example.librarymanagement.data.TestDataFactory
import com.example.librarymanagement.dto.AuthorDto
import com.example.librarymanagement.dto.mapper.AuthorMapper
import com.example.librarymanagement.dto.mapper.ErrorMapper
import com.example.librarymanagement.exception.EntityNotFoundException
import com.example.librarymanagement.exception.GlobalExceptionHandler
import com.example.librarymanagement.model.entity.Author
import com.example.librarymanagement.service.AuthorService
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

class AuthorControllerTest {

    private lateinit var mockMvc: MockMvc
    private lateinit var authorService: AuthorService
    private lateinit var authorController: AuthorController
    private lateinit var authorMapper: AuthorMapper
    private lateinit var globalExceptionHandler: GlobalExceptionHandler
    private lateinit var errorMapper: ErrorMapper
    private val objectMapper: ObjectMapper = ObjectMapper()

    private var author = TestDataFactory.createAuthor()
    private lateinit var authorDto: AuthorDto
    private var errorDto = ErrorMapper().toErrorDto(HttpStatus.BAD_REQUEST, "Invalid")

    @BeforeEach
    fun setUp() {
        authorService = mockk(relaxed = true)
        authorController = mockk(relaxed = true)
        authorMapper = mockk(relaxed = true)
        errorMapper = mockk(relaxed = true)
        globalExceptionHandler = GlobalExceptionHandler(errorMapper)
        authorController = AuthorController(authorService, authorMapper)

        authorDto = AuthorMapper().toAuthorDto(author)

        mockMvc = MockMvcBuilders.standaloneSetup(authorController)
            .setControllerAdvice(globalExceptionHandler)
            .build()
    }

    @Test
    fun shouldCreateAuthor() {
        val expected = objectMapper.writeValueAsString(authorDto)

        every { authorService.createAuthor(any()) } returns author
        every { authorMapper.toAuthorDto(any<Author>()) } returns authorDto

        val result = mockMvc.perform(post("/api/v1/author")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(authorDto)))
            .andExpect(status().isCreated())
            .andReturn().response.contentAsString

        Assertions.assertEquals(expected, result)
    }

    @Test
    fun shouldNotCreateAuthorWithInvalidData() {
        val authorDtoNew = authorDto.copy(lastName = "")

        every { authorService.createAuthor(any()) } throws IllegalArgumentException("Invalid")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto

        val result = mockMvc.perform(post("/api/v1/author")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(authorDtoNew)))
            .andExpect(status().isBadRequest())
            .andReturn().response.contentAsString

        Assertions.assertEquals(objectMapper.writeValueAsString(errorDto), result)
    }

    @Test
    fun shouldUpdateAuthor() {
        val expected = objectMapper.writeValueAsString(authorDto)

        every { authorService.updateAuthor(any(), any()) } returns author
        every { authorMapper.toAuthorDto(any<Author>()) } returns authorDto

        val result = mockMvc.perform(put("/api/v1/author/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(authorDto)))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        Assertions.assertEquals(expected, result)
    }

    @Test
    fun shouldNotUpdateAuthorWithInvalidData() {
        val authorDtoNew = authorDto.copy(lastName = "")

        every { authorService.updateAuthor(any(), any()) } throws IllegalArgumentException("Invalid")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto

        val result = mockMvc.perform(put("/api/v1/author/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(authorDtoNew)))
            .andExpect(status().isBadRequest())
            .andReturn().response.contentAsString

        Assertions.assertEquals(objectMapper.writeValueAsString(errorDto), result)
    }

    @Test
    fun shouldGetAllAuthors() {
        val expected = objectMapper.writeValueAsString(listOf(authorDto))

        every { authorService.getAllAuthors() } returns listOf(author)
        every { authorMapper.toAuthorDto(any<List<Author>>()) } returns listOf(authorDto)

        val result = mockMvc.perform(get("/api/v1/author"))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        Assertions.assertEquals(expected, result)
    }

    @Test
    fun shouldGetAuthorById() {
        val expected = objectMapper.writeValueAsString(authorDto)

        every { authorService.getAuthorById(any()) } returns author
        every { authorMapper.toAuthorDto(any<Author>()) } returns authorDto

        val result = mockMvc.perform(get("/api/v1/author/{id}", 1L))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        Assertions.assertEquals(expected, result)
    }

    @Test
    fun shouldNotGetAuthorById() {
        every { authorService.getAuthorById(any()) } throws EntityNotFoundException("Author")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto

        mockMvc.perform(get("/api/v1/author/{id}", 1L))
            .andExpect(status().isNotFound)
    }
}