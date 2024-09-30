package com.example.librarymanagement.controller

import com.example.librarymanagement.controller.rest.BookController
import com.example.librarymanagement.data.BookDataFactory
import com.example.librarymanagement.data.ErrorDataFactory
import com.example.librarymanagement.dto.AuthorDto
import com.example.librarymanagement.dto.BookDto
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
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class BookControllerTest {
    private lateinit var webTestClient: WebTestClient

    private val bookService: BookService = mockk()
    private val bookMapper: BookMapper = mockk(relaxed = true)
    private val errorMapper: ErrorMapper = mockk()
    private val objectMapper: ObjectMapper = ObjectMapper()
    private val bookController = BookController(bookService, bookMapper)

    private var book = BookDataFactory.createBook(ID)
    private var bookRequestDto = BookDataFactory.createBookRequestDto(ID)
    private var bookResponseDto = BookMapper().toBookDto(book)
    private var errorDtoBadRequest = ErrorDataFactory.createBadRequestError()

    @BeforeEach
    fun setUp() {
        webTestClient = WebTestClient
            .bindToController(bookController)
            .controllerAdvice(GlobalExceptionHandler(errorMapper))
            .build()
    }

    @Test
    fun `should create book`() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(bookResponseDto)
        val content = objectMapper.writeValueAsString(bookRequestDto)

        every { bookService.createBook(any()) } returns Mono.just(book)
        every { bookMapper.toBookDto(any<Book>()) } returns bookResponseDto

        //WHEN
        val actual = webTestClient.post()
            .uri(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(content)
            .exchange()
            .expectStatus().isCreated
            .expectBody(BookDto::class.java)
            .returnResult().responseBody

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should not create book with invalid data`() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(errorDtoBadRequest)

        every { bookService.createBook(any()) } throws IllegalArgumentException("Invalid")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDtoBadRequest

        //WHEN
        val actual = webTestClient.post()
            .uri(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(expected)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(BookDto::class.java)
            .returnResult().responseBody

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should update book`() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(bookResponseDto)

        every { bookService.updateBook(any()) } returns Mono.just(book)
        every { bookMapper.toBookDto(any<Book>()) } returns bookResponseDto

        //WHEN
        val actual = webTestClient.put()
            .uri("${URL}/{id}", ID)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(expected)
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java)
            .returnResult().responseBody

        //THEN
        Assertions.assertEquals(expected, actual)
    }

//
//    @Test
//    fun shouldNotUpdateAuthorWithInvalidData() {
//        //GIVEN
//        val expected = objectMapper.writeValueAsString(errorDtoBadRequest)
//        val content = objectMapper.writeValueAsString(authorDto.copy(firstName = ""))
//
//        every { authorService.updateAuthor(any()) } throws IllegalArgumentException("Invalid")
//        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDtoBadRequest
//
//        //WHEN
//        val actual = mockMvc.perform(
//            put("$URL/{id}", ID)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(content)
//        )
//            .andExpect(status().isBadRequest())
//            .andReturn().response.contentAsString
//
//        //THEN
//        Assertions.assertEquals(expected, actual)
//    }

    @Test
    fun `should not update book with invalid data`() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(errorDtoBadRequest)

        every { bookService.updateBook(any()) } throws IllegalArgumentException("Invalid")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDtoBadRequest

        //WHEN
        val actual = webTestClient.put()
            .uri("$URL/{id}", ID)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(expected)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(BookDto::class.java)
            .returnResult().responseBody

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should get all books`() {
        // Given
        val expected = listOf(bookResponseDto)

        every { bookService.getAll() } returns Flux.fromIterable(listOf(book))
        every { bookMapper.toBookDto(book) } returns bookResponseDto

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
        val expected = objectMapper.writeValueAsString(bookResponseDto)

        every { bookService.getBookById(any()) } returns Mono.just(book)
        every { bookMapper.toBookDto(any<Book>()) } returns bookResponseDto

        //WHEN
        val actual = webTestClient.get()
            .uri("${URL}/{id}", ID)
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java)
            .returnResult().responseBody

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should delete book`() {
        every { bookService.deleteBookById(any()) } returns Mono.empty()

        webTestClient
            .delete()
            .uri("$URL/{ID}", ID)
            .exchange()
            .expectStatus().isNoContent
    }

    @Test
    fun `should get book by title and author`() {
        // GIVEN
        val expected = objectMapper.writeValueAsString(bookResponseDto)

        every { bookService.getBookByTitleAndAuthor("Title", ID) } returns Flux.fromIterable(listOf(book))
        every { bookMapper.toBookDto(any<Book>()) } returns bookResponseDto

        // WHEN
        val actual = webTestClient.get()
            .uri { uriBuilder ->
                uriBuilder.path(URL)
                    .queryParam("title", "Title")
                    .queryParam("author", ID)
                    .build()
            }
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .returnResult().responseBody

        // THEN
        Assertions.assertEquals(expected, actual)
    }

    companion object {
        const val ID = BookDataFactory.JPA_ID.toString()
        const val URL = "/api/v1/book"
    }
}
