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

class BookServiceImplTest {
    private val bookRepository: BookRepository = mockk()
    private val authorService: AuthorService = mockk()
    private val publisherService: PublisherService = mockk()
    private val bookService = BookServiceImpl(
        bookRepository,
        authorService,
        publisherService
    )
    private val id = "1"

    private val book = BookDataFactory.createBook(id)
    private val author = AuthorDataFactory.createAuthor(id)
    private val publisher = PublisherDataFactory.createPublisher(id)

    @Test
    fun shouldCreateBook() {
        //GIVEN
        val expected = book

        every { bookRepository.existsByIsbn(book.isbn) } returns false
        every { authorService.getAuthorById(id) } returns author
        every { publisherService.getPublisherById(id) } returns publisher
        every { bookRepository.save(book) } returns book

        //WHEN
        val actual = bookService.createBook(book)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { bookRepository.save(book) }
    }

    @Test
    fun shouldGetBookById() {
        //GIVEN
        val expected = book
        every { bookRepository.findById(id) } returns book

        //WHEN
        val actual = bookService.getBookById(id)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { bookRepository.findById(id) }
    }

    @Test
    fun shouldGetAllBooks() {
        //GIVEN
        val expected = listOf(book)
        every { bookRepository.findAll() } returns expected

        //WHEN
        val actual = bookService.findAll()

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { bookRepository.findAll() }
    }

    @Test
    fun shouldUpdateBook() {
        //GIVEN
        val expected = book.copy(title = "Updated Book")

        every { bookRepository.existsByIsbn(book.isbn) } returns false
        every { authorService.getAuthorById(id) } returns author
        every { publisherService.getPublisherById(id) } returns publisher
        every { bookService.getBookById(id) } returns book
        every { bookRepository.save(expected) } returns expected

        //WHEN
        val actual = bookService.updateBook(expected)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { bookRepository.existsByIsbn(book.isbn) }
        verify(exactly = 1) { authorService.getAuthorById(id) }
        verify(exactly = 1) { publisherService.getPublisherById(id) }
        verify(exactly = 1) { bookService.getBookById(id) }
        verify(exactly = 1) { bookRepository.save(expected) }
    }

    @Test
    fun shouldDeleteBookById() {
        //GIVEN
        every { bookRepository.deleteById(id) } returns Unit

        //WHEN
        bookService.deleteBookById(id)

        //THEN
        verify(exactly = 1) { bookRepository.deleteById(id) }
    }

    @Test
    fun shouldGetBookByTitleAndAuthor() {
        //GIVEN
        val expected = book

        every { bookRepository.findBookByTitleAndAuthorId(any(), any()) } returns book

        //WHEN
        val actual = bookService.getBookByTitleAndAuthor("Book", id)

        //THEN
        Assertions.assertEquals(expected, actual)
    }
}
