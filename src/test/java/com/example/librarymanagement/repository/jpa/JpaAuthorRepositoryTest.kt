package com.example.librarymanagement.repository.jpa

import com.example.librarymanagement.data.AuthorDataFactory
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

class JpaAuthorRepositoryTest {
    private val authorRepositorySpring: AuthorRepositorySpring = mockk()
    private val jpaAuthorRepository = JpaAuthorRepository(authorRepositorySpring)
    private val jpaAuthor = AuthorDataFactory.createJpaAuthor()

    private val author = AuthorDataFactory.createAuthor()

    @Test
    fun shouldFindAllAuthors() {
        //GIVEN
        val expected = listOf(author)

        every { authorRepositorySpring.findAll() } returns listOf(jpaAuthor)

        //WHEN
        val actual = jpaAuthorRepository.findAll()

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldCreateAuthor() {
        //GIVEN
        val expected = author

        every { authorRepositorySpring.save(any()) } returns jpaAuthor

        //WHEN
        val actual = jpaAuthorRepository.save(expected)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { authorRepositorySpring.save(any()) }
    }

    @Test
    fun shouldFindById() {
        //GIVEN
        val expected = author

        every { authorRepositorySpring.findById(ID) } returns Optional.of(jpaAuthor)

        //WHEN
        val actual = jpaAuthorRepository.findById(ID.toString())

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    companion object {
        const val ID = AuthorDataFactory.JPA_ID
    }
}
