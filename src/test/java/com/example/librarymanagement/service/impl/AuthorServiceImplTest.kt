package com.example.librarymanagement.service.impl

import com.example.librarymanagement.data.AuthorDataFactory
import com.example.librarymanagement.repository.AuthorRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class AuthorServiceImplTest {
    private val id = "1"

    private val authorRepository: AuthorRepository = mockk()
    private val authorService = AuthorServiceImpl(authorRepository)
    private val author = AuthorDataFactory.createAuthor(id)

    @Test
    fun shouldFindAll() {
        //GIVEN
        val expected = listOf(author)

        every { authorRepository.findAll() } returns (listOf(author))

        //WHEN
        val actual = authorService.getAllAuthors()

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { authorRepository.findAll() }
    }

    @Test
    fun shouldFindById() {
        //GIVEN
        val expected = author

        every { authorRepository.findById(any()) } returns author

        //WHEN
        val actual = authorService.getAuthorById(1L.toString())

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { authorRepository.findById(1L.toString()) }
    }

    @Test
    fun shouldCreateAuthor() {
        //GIVEN
        val expected = author

        every { authorRepository.save(author) } returns author

        //WHEN
        val actual = authorService.createAuthor(author)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { authorRepository.save(author) }
    }

    @Test
    fun shouldUpdateAuthor() {
        //GIVEN
        val expected = author.copy(firstName = "Updated")

        every { authorRepository.findById(1L.toString()) } returns author
        every { authorRepository.save(expected) } returns expected

        //WHEN
        val actual = authorService.updateAuthor(expected)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { authorRepository.findById(1L.toString()) }
        verify(exactly = 1) { authorRepository.save(expected) }
    }
}
