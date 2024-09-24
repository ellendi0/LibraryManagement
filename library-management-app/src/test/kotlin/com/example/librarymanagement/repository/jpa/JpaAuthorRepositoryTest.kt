package com.example.librarymanagement.repository.jpa

import com.example.librarymanagement.data.AuthorDataFactory
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import reactor.test.StepVerifier
import java.util.*

class JpaAuthorRepositoryTest {
    private val authorRepositorySpring: AuthorRepositorySpring = mockk()
    private val jpaAuthorRepository = JpaAuthorRepository(authorRepositorySpring)
    private val jpaAuthor = AuthorDataFactory.createJpaAuthor()

    private val author = AuthorDataFactory.createAuthor()

    @Test
    fun `should find all authors`() {
        //GIVEN
        val expected = author

        every { authorRepositorySpring.findAll() } returns listOf(jpaAuthor)

        //WHEN
        val result = StepVerifier.create(jpaAuthorRepository.findAll())

        //THEN
        result
            .assertNext { actual -> Assertions.assertEquals(expected, actual) }
            .verifyComplete()
    }

    @Test
    fun `should create author`() {
        //GIVEN
        val expected = author

        every { authorRepositorySpring.save(any()) } returns jpaAuthor

        //WHEN
        val result = StepVerifier.create(jpaAuthorRepository.save(expected))

        //THEN
        result
            .assertNext { actual ->
                Assertions.assertEquals(expected, actual)
                verify(exactly = 1) { authorRepositorySpring.save(any())}
            }
            .verifyComplete()
    }

    @Test
    fun `should find by id`() {
        //GIVEN
        val expected = author

        every { authorRepositorySpring.findById(ID) } returns Optional.of(jpaAuthor)

        //WHEN
        val result = StepVerifier.create(jpaAuthorRepository.findById(ID.toString()))

        //THEN
        result
            .assertNext { actual ->
                Assertions.assertEquals(expected, actual)
                verify(exactly = 1) { authorRepositorySpring.findById(ID)}
            }
            .verifyComplete()
    }

    companion object {
        const val ID = AuthorDataFactory.JPA_ID
    }
}
