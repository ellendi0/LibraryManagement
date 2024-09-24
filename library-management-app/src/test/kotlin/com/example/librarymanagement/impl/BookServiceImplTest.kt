package com.example.librarymanagement.service.impl

import com.example.librarymanagement.data.AuthorDataFactory
import com.example.librarymanagement.data.BookDataFactory
import com.example.librarymanagement.data.PublisherDataFactory
import com.example.librarymanagement.repository.BookRepository
import com.example.librarymanagement.service.AuthorService
import com.example.librarymanagement.service.PublisherService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class BookServiceImplTest {
    private val bookRepository: BookRepository = mockk()
    private val authorService: AuthorService = mockk()
    private val publisherService: PublisherService = mockk()
    private val bookService = BookServiceImpl(
        bookRepository,
        authorService,
        publisherService
    )
    private val id = BookDataFactory.ID

    private val book = BookDataFactory.createBook(id)
    private val author = AuthorDataFactory.createAuthor(id)
    private val publisher = PublisherDataFactory.createPublisher(id)

    //    fun findAll(): Flux<Book>
    //    fun getBookById(id: String): Mono<Book>
    //    fun getBookByTitleAndAuthor(title: String, authorId: String): Flux<Book>
    //    fun createBook(book: Book): Mono<Book>
    //    fun updateBook(updatedBook: Book): Mono<Book>
    //    fun deleteBookById(id: String): Mono<Unit>
    //    fun existsBookById(id: String): Mono<Boolean>

    @Test
    fun `should find all`() {
        // GIVEN
        val expected = book

        every { bookRepository.findAll() } returns Flux.just(expected)

        // WHEN
        val result = StepVerifier.create(bookService.findAll())

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
        }.verifyComplete()
    }

    @Test
    fun `should create book`() {
        // GIVEN
        val expected = book

        every { bookRepository.existsByIsbn(book.isbn) } returns Mono.just(false)
        every { authorService.getAuthorById(id) } returns Mono.just(author)
        every { publisherService.getPublisherById(id) } returns Mono.just(publisher)
        every { bookRepository.save(book) } returns Mono.just(book)

        // WHEN
        val result = StepVerifier.create(bookService.createBook(book))

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
            verify(exactly = 1) { bookRepository.save(book) }
        }.verifyComplete()
    }

    @Test
    fun `should get book by id`() {
        // GIVEN
        val expected = book
        every { bookRepository.findById(id) } returns Mono.just(book)

        // WHEN
        val result = StepVerifier.create(bookService.getBookById(id))

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
        }.verifyComplete()
    }

    @Test
    fun `should get all books`() {
        // GIVEN
        val expected = book

        every { bookRepository.findAll() } returns Flux.just(book)

        // WHEN
        val result = StepVerifier.create(bookService.findAll())

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
        }.verifyComplete()
    }

    @Test
    fun `should update book`() {
        // GIVEN
        val expected = book

        every { bookRepository.existsByIsbn(book.isbn) } returns Mono.just(false)
        every { authorService.getAuthorById(id) } returns Mono.just(author)
        every { publisherService.getPublisherById(id) } returns Mono.just(publisher)
        every { bookRepository.findById(id) } returns Mono.just(expected)
        every { bookRepository.save(book) } returns Mono.just(expected)

        // WHEN
        val result = StepVerifier.create(bookService.updateBook(book))

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
            verify(exactly = 1) { bookRepository.save(book) }
        }.verifyComplete()
    }


    @Test
    fun `should delete book by id`() {
        // GIVEN
        val expected = Unit
        every { bookRepository.deleteById(id) } returns Mono.just(Unit)

        // WHEN
        val result = StepVerifier.create(bookService.deleteBookById(id))

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
            verify(exactly = 1) { bookRepository.deleteById(id) }
        }.verifyComplete()
    }

    @Test
    fun `should get book by title and author`() {
        // GIVEN
        val expected = book

        every { bookRepository.findBookByTitleAndAuthorId(any(), any()) } returns Flux.just(book)

        // WHEN
        val result = StepVerifier.create(bookService.getBookByTitleAndAuthor("book", id))

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
        }.verifyComplete()
    }

    @Test
    fun `should exists book by id`() {
        // GIVEN
        val expected = true

        every { bookRepository.existsById(any()) } returns Mono.just(true)

        // WHEN
        val result = StepVerifier.create(bookService.existsBookById(id))

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
        }.verifyComplete()
    }
}
