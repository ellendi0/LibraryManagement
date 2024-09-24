package com.example.librarymanagement.service.impl

import com.example.librarymanagement.data.AuthorDataFactory
import com.example.librarymanagement.repository.AuthorRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class AuthorServiceImplTest {
    private val id = AuthorDataFactory.ID

    private val authorRepository: AuthorRepository = mockk()
    private val authorService = AuthorServiceImpl(authorRepository)
    private val author = AuthorDataFactory.createAuthor(id)

    @Test
    fun `should find all author`() {
        // GIVEN
        val expected = author

        every { authorRepository.findAll() } returns Flux.just(author)

        // WHEN
        val result = StepVerifier.create(authorService.getAllAuthors())

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
            verify(exactly = 1) { authorRepository.findAll() }
        }.verifyComplete()
    }


    @Test
    fun `should find by id `() {
        // GIVEN
        val expected = author

        every { authorRepository.findById(any()) } returns Mono.just(author)

        // WHEN
        val result = StepVerifier.create(authorService.getAuthorById(id))

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
            verify(exactly = 1) { authorRepository.findById(any()) }
        }.verifyComplete()
    }

    @Test
    fun `should create author`() {
        // GIVEN
        val expected = author

        every { authorRepository.save(author) } returns Mono.just(author)

        // WHEN
        val result = StepVerifier.create(authorService.createAuthor(author))

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
            verify(exactly = 1) { authorRepository.save(author) }
        }.verifyComplete()
    }

    @Test
    fun `should update author`() {
        // GIVEN
        val expected = author

        every { authorRepository.findById(id) } returns Mono.just(author)
        every { authorRepository.save(expected) } returns Mono.just(expected)

        // WHEN
        val result = StepVerifier.create(authorService.updateAuthor(expected))

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
            verify(exactly = 1) { authorRepository.findById(id) }
            verify(exactly = 1) { authorRepository.save(expected) }
        }.verifyComplete()
    }
}
