package com.example.librarymanagement.controller

import com.example.librarymanagement.data.AuthorDataFactory
import com.example.librarymanagement.data.ErrorDataFactory
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

    private var author = AuthorDataFactory.createAuthor()
    private var authorDto: AuthorDto = AuthorMapper().toAuthorDto(author)
    private var errorDtoBadRequest = ErrorDataFactory.createBadRequestError()
    private val errorDtoNotFound = ErrorDataFactory.createNotFoundError()

    @BeforeEach
    fun setUp() {
        authorService = mockk(relaxed = true)
        authorController = mockk(relaxed = true)
        authorMapper = mockk(relaxed = true)
        errorMapper = mockk(relaxed = true)
        globalExceptionHandler = GlobalExceptionHandler(errorMapper)
        authorController = AuthorController(authorService, authorMapper)

        mockMvc = MockMvcBuilders.standaloneSetup(authorController)
            .setControllerAdvice(globalExceptionHandler)
            .build()
    }

    @Test
    fun shouldCreateAuthor() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(authorDto)

        every { authorService.createAuthor(any()) } returns author
        every { authorMapper.toAuthorDto(any<Author>()) } returns authorDto

        //WHEN
        val actual = mockMvc.perform(post("/api/v1/author")
            .contentType(MediaType.APPLICATION_JSON)
            .content(expected))
            .andExpect(status().isCreated())
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldNotCreateAuthorWithInvalidData() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(errorDtoBadRequest)
        val content = objectMapper.writeValueAsString(authorDto.copy(lastName = ""))

        every { authorService.createAuthor(any()) } throws IllegalArgumentException("Invalid")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDtoBadRequest

        //WHEN
        val actual = mockMvc.perform(post("/api/v1/author")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isBadRequest())
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldUpdateAuthor() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(authorDto)

        every { authorService.updateAuthor(any()) } returns author
        every { authorMapper.toAuthorDto(any<Author>()) } returns authorDto

        //WHEN
        val actual = mockMvc.perform(put("/api/v1/author/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(expected))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldNotUpdateAuthorWithInvalidData() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(errorDtoBadRequest)
        val content = objectMapper.writeValueAsString(authorDto.copy(firstName = ""))

        every { authorService.updateAuthor(any()) } throws IllegalArgumentException("Invalid")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDtoBadRequest

        //WHEN
        val actual = mockMvc.perform(put("/api/v1/author/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isBadRequest())
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldGetAllAuthors() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(listOf(authorDto))

        every { authorService.getAllAuthors() } returns listOf(author)
        every { authorMapper.toAuthorDto(any<List<Author>>()) } returns listOf(authorDto)

        //WHEN
        val actual = mockMvc.perform(get("/api/v1/author"))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldGetAuthorById() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(authorDto)

        every { authorService.getAuthorById(any()) } returns author
        every { authorMapper.toAuthorDto(any<Author>()) } returns authorDto

        //WHEN
        val actual = mockMvc.perform(get("/api/v1/author/{id}", 1L))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldNotGetAuthorById() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(errorDtoNotFound)

        every { authorService.getAuthorById(any()) } throws EntityNotFoundException("Author")
        every { errorMapper.toErrorDto(any(), any<String>()) } returns errorDtoNotFound

        //WHEN
        val actual = mockMvc.perform(get("/api/v1/author/{id}", 1L))
            .andExpect(status().isNotFound())
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }
}
