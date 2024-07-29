package com.example.librarymanagement.service.impl

import com.example.librarymanagement.data.TestDataFactory
import com.example.librarymanagement.model.entity.Book
import com.example.librarymanagement.model.entity.Journal
import com.example.librarymanagement.model.entity.Library
import com.example.librarymanagement.model.entity.Reservation
import com.example.librarymanagement.model.entity.User
import com.example.librarymanagement.repository.BookRepository
import com.example.librarymanagement.service.AuthorService
import com.example.librarymanagement.service.BookPresenceService
import com.example.librarymanagement.service.PublisherService
import com.example.librarymanagement.service.ReservationService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class BookServiceImplTest {
    private val bookRepository: BookRepository = mockk()
    private val authorService: AuthorService = mockk()
    private val publisherService: PublisherService = mockk()
    private val reservationService: ReservationService = mockk()
    private val bookPresenceService: BookPresenceService = mockk()
    private val bookService = BookServiceImpl(
        bookRepository,
        authorService,
        publisherService,
        bookPresenceService,
        reservationService)

    private lateinit var book: Book
    private lateinit var library: Library
    private lateinit var user : User
    private lateinit var journal: Journal
    private lateinit var reservation: Reservation

    @BeforeEach
    fun setUp() {
        var test = TestDataFactory.createTestDataRelForServices()
        book = test.book
        user = test.user
        library = test.library
        journal = test.journal
        reservation = test.reservation
    }

    @Test
    fun shouldCreateBook() {
        every { bookRepository.existsByIsbn(book.isbn) } returns false
        every { authorService.getAuthorById(1L) } returns book.author!!
        every { publisherService.getPublisherById(1L) } returns book.publisher!!
        every { bookRepository.save(book) } returns book

        Assertions.assertEquals(book, bookService.createBook(1L, 1L, book))
        verify (exactly = 1) { bookRepository.save(book) }
    }

    @Test
    fun shouldGetBookById() {
        every { bookRepository.findById(1L) } returns Optional.of(book)

        Assertions.assertEquals(book, bookService.getBookById(1L))
        verify (exactly = 1) { bookRepository.findById(1L) }
    }

    @Test
    fun shouldGetAllBooks() {
        val books = listOf(book)
        every { bookRepository.findAll() } returns books

        Assertions.assertEquals(books, bookService.findAll())
        verify (exactly = 1) { bookRepository.findAll() }
    }

    @Test
    fun shouldUpdateBook() {
        val updatedBook = book.copy(title = "Updated Book")
        every { bookRepository.findById(1L) } returns Optional.of(book)
        every { bookRepository.save(updatedBook) } returns updatedBook

        Assertions.assertEquals(updatedBook, bookService.updateBook(1L, updatedBook))
        verify (exactly = 1) { bookRepository.findById(1L) }
        verify (exactly = 1) { bookRepository.save(updatedBook) }
    }

    @Test
    fun shouldDeleteBook() {
        every { bookRepository.findById(1L) } returns Optional.of(book)
        every { bookPresenceService.deleteBookPresenceById(1L) } returns Unit
        every { reservationService.deleteReservationById(1L) } returns Unit
        every { bookRepository.deleteById(1L) } returns Unit

        bookService.deleteBook(1L)
        verify (exactly = 1) { bookRepository.findById(1L) }
        verify (exactly = 1) { bookPresenceService.deleteBookPresenceById(1L) }
        verify (exactly = 1) { reservationService.deleteReservationById(1L) }
        verify (exactly = 1) { bookRepository.deleteById(1L) }
    }
}