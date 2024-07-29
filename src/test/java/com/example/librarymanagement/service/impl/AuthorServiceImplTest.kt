package com.example.librarymanagement.service.impl

import com.example.librarymanagement.data.TestDataFactory
import com.example.librarymanagement.repository.AuthorRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

class AuthorServiceImplTest {
    private val authorRepository: AuthorRepository = mockk()
    private val authorService = AuthorServiceImpl(authorRepository)
    private val author = TestDataFactory.createAuthor()

    @Test
    fun shouldFindAll(){
        every { authorRepository.findAll() } returns(listOf(author))

        Assertions.assertEquals(listOf(author), authorService.getAllAuthors())
        verify(exactly = 1){ authorRepository.findAll() }
    }

    @Test
    fun shouldFindById(){
        every { authorRepository.findById(1) } returns Optional.of(author)

        Assertions.assertEquals(author, authorService.getAuthorById(1))
        verify(exactly = 1){ authorRepository.findById(1) }
    }

    @Test
    fun shouldCreateAuthor(){
        every { authorRepository.save(author) } returns author

        Assertions.assertEquals(author, authorService.createAuthor(author))
        verify(exactly = 1){ authorRepository.save(author) }
    }

    @Test
    fun shouldUpdateAuthor(){
        val updatedAuthor = author.copy(firstName = "Updated")
        every { authorRepository.findById(1) } returns Optional.of(author)
        every { authorRepository.save(updatedAuthor) } returns updatedAuthor

        Assertions.assertEquals(updatedAuthor, authorService.updateAuthor(1, updatedAuthor))
        verify(exactly = 1){ authorRepository.findById(1) }
        verify(exactly = 1){ authorRepository.save(updatedAuthor) }
    }

}