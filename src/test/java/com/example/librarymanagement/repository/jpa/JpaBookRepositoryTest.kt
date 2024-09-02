package com.example.librarymanagement.repository.jpa

import com.example.librarymanagement.data.AuthorDataFactory
import com.example.librarymanagement.data.BookDataFactory
import com.example.librarymanagement.data.PublisherDataFactory
import com.example.librarymanagement.data.TestDataFactory
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.Optional

class JpaBookRepositoryTest {
    private val bookRepositorySpring: BookRepositorySpring = mockk()
    private val authorRepository: JpaAuthorRepository = mockk()
    private val publisherRepository: JpaPublisherRepository = mockk()
    private val jpaBookRepository = JpaBookRepository(bookRepositorySpring, authorRepository, publisherRepository)
    private val jpaBook = TestDataFactory.createTestDataRelationsForJpaRepositories().book

    private val jpaId = BookDataFactory.JPA_ID
    private val id = jpaId.toString()

    private val book = BookDataFactory.createBook(jpaId)

    @Test
    fun `should save book`() {
        //GIVEN
        val expected = book
        val author = AuthorDataFactory.createAuthor(jpaId)
        val publisher = PublisherDataFactory.createPublisher(jpaId)

        every { authorRepository.findById(id) } returns author
        every { publisherRepository.findById(any()) } returns publisher
        every { bookRepositorySpring.save(jpaBook) } returns jpaBook

        //WHEN
        val actual = jpaBookRepository.save(book)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { bookRepositorySpring.save(jpaBook) }
    }

    @Test
    fun `should find by id`() {
        //GIVEN
        val expected = book

        every { bookRepositorySpring.findById(jpaId) } returns Optional.of(jpaBook)

        //WHEN
        val actual = jpaBookRepository.findById(id)

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should find all books`() {
        //GIVEN
        val expected = listOf(book)

        every { bookRepositorySpring.findAll() } returns listOf(jpaBook)

        //WHEN
        val actual = jpaBookRepository.findAll()

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should delete book by id`() {
        //GIVEN
        every { bookRepositorySpring.deleteById(jpaId) } returns Unit

        //WHEN
        jpaBookRepository.deleteById(id)

        //THEN
        verify { bookRepositorySpring.deleteById(jpaId) }
    }

    @Test
    fun `should return true if exists by isbn`() {
        //GIVEN
        val isbn = 1111111111111L

        every { bookRepositorySpring.existsById(isbn) } returns true

        //WHEN
        jpaBookRepository.existsByIsbn(isbn)

        //THEN
        verify { bookRepositorySpring.existsById(isbn) }
    }

    @Test
    fun `should find book by title and authorId`() {
        //GIVEN
        val expected = book

        every { bookRepositorySpring.findBookByTitleAndAuthorId(any(), any()) } returns jpaBook

        //WHEN
        val actual = jpaBookRepository.findBookByTitleAndAuthorId("Title", id)

        //THEN
        Assertions.assertEquals(expected, actual)
    }
}
