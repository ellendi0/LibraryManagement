package com.example.librarymanagement.controller

import com.example.librarymanagement.controller.rest.AuthorController
import com.example.librarymanagement.data.AuthorDataFactory
import com.example.librarymanagement.data.ErrorDataFactory
import com.example.librarymanagement.dto.AuthorDto
import com.example.librarymanagement.dto.ErrorDto
import com.example.librarymanagement.dto.mapper.AuthorMapper
import com.example.librarymanagement.dto.mapper.ErrorMapper
import com.example.librarymanagement.exception.GlobalExceptionHandler
import com.example.librarymanagement.model.domain.Author
import com.example.librarymanagement.service.AuthorService
import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class AuthorControllerTest {
    private lateinit var webTestClient: WebTestClient

    private val authorService: AuthorService = mockk()
    private val authorMapper: AuthorMapper = mockk(relaxed = true)
    private val errorMapper: ErrorMapper = mockk()
    private val objectMapper: ObjectMapper = ObjectMapper()
    private val authorController = AuthorController(authorService, authorMapper)

    private var author = AuthorDataFactory.createAuthor(ID)
    private var authorDto: AuthorDto = AuthorMapper().toAuthorDto(author)
    private var errorDtoBadRequest = ErrorDataFactory.createBadRequestError()
    private val errorDtoNotFound = ErrorDataFactory.createNotFoundError()

    @BeforeEach
    fun setUp() {
        webTestClient = WebTestClient
            .bindToController(authorController)
            .controllerAdvice(GlobalExceptionHandler(errorMapper))
            .build()
    }

    @Test
    fun `should create author`() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(authorDto)

        every { authorService.createAuthor(any()) } returns Mono.just(author)
        every { authorMapper.toAuthorDto(any<Author>()) } returns authorDto

        //WHEN
        val actual = webTestClient.post()
            .uri(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(expected)
            .exchange()
            .expectStatus().isCreated
            .expectBody(String::class.java)
            .returnResult().responseBody

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should not create author with invalid data`() {
        // GIVEN
        val invalidAuthorDto = authorDto.copy(firstName = "")  // Invalid because the name is empty
        val expectedErrorDto = errorDtoBadRequest // Use ErrorDto object for comparison

        every { authorService.createAuthor(any()) } returns Mono.error(IllegalStateException("error message"))
        every { errorMapper.toErrorDto(HttpStatus.BAD_REQUEST, any<List<String>>()) } returns expectedErrorDto

        // WHEN
        val actual = webTestClient.post()
            .uri(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(objectMapper.writeValueAsString(invalidAuthorDto))
            .exchange()
            .expectStatus().isBadRequest
            .expectBody<ErrorDto>()
            .returnResult().responseBody

        // THEN
        Assertions.assertEquals(expectedErrorDto, actual)
    }




    @Test
    fun `should update author`() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(authorDto)

        every { authorService.updateAuthor(any()) } returns Mono.just(author)
        every { authorMapper.toAuthorDto(any<Author>()) } returns authorDto

        //WHEN
        val actual = webTestClient.put()
            .uri("$URL/{id}", ID)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(expected)
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java)
            .returnResult().responseBody

        //THEN
        Assertions.assertEquals(expected, actual)
    }

//    @Test
//    fun `should not update author with invalid data`() {
//        //GIVEN
//        val expected = objectMapper.writeValueAsString(errorDtoBadRequest)
//
//        every { authorService.updateAuthor(any()) } throws IllegalArgumentException("Invalid")
//        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDtoBadRequest
//
//        //WHEN
//        webTestClient.put()
//            .uri("${URL}/{id}", ID)
//            .contentType(MediaType.APPLICATION_JSON)
//            .bodyValue(expected)
//            .exchange()
//            .expectStatus().is4xxClientError
//            .expectBody(errorDtoBadRequest).isEqualTo(expected)
//
//        //THEN
////        Assertions.assertEquals(expected, actual)
//    }

    @Test
    fun `should get all authors`() {
        // Given
        val expected = listOf(authorDto)

        every { authorService.getAllAuthors() } returns Flux.fromIterable(listOf(author))
        every { authorMapper.toAuthorDto(author) } returns authorDto

        // When
        val actual = webTestClient.get()
            .uri(URL)
            .exchange()
            .expectStatus().isOk
            .expectBodyList(AuthorDto::class.java)
            .returnResult().responseBody

        // Then
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should find by id`() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(authorDto)

        every { authorService.getAuthorById(any()) } returns Mono.just(author)
        every { authorMapper.toAuthorDto(any<Author>()) } returns authorDto

        //WHEN
        val actual = webTestClient.get()
            .uri("$URL/{id}", ID)
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java)
            .returnResult().responseBody

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should not find by id`() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(authorDto)

        every { authorService.getAuthorById(any()) } returns Mono.just(author)
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDtoBadRequest

        //WHEN
        val actual = webTestClient.get()
            .uri("$URL/{id}", ID)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult().responseBody

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    companion object {
        const val ID = AuthorDataFactory.JPA_ID.toString()
        const val URL = "/api/v1/author"
    }
}


//// GIVEN
//        val expected = objectMapper.writeValueAsString(errorDtoBadRequest)
//
//        every { authorService.createAuthor(any()) } throws IllegalArgumentException("Invalid")
//        every { errorMapper.toErrorDto(eq(HttpStatus.BAD_REQUEST), eq(listOf("Invalid"))) } returns errorDtoBadRequest
//
//        // WHEN
//        val actual = webTestClient.post()
//            .uri(URL)
//            .contentType(MediaType.APPLICATION_JSON)
//            .bodyValue(authorDto)
//            .exchange()
//            .expectStatus().isBadRequest
//            .expectBody(ErrorDto::class.java)
//            .returnResult().responseBody
//
//        // THEN
//        Assertions.assertEquals(expected, objectMapper.writeValueAsString(actual))
