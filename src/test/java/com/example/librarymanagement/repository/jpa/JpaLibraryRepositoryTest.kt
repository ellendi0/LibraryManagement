package com.example.librarymanagement.repository.jpa

import com.example.librarymanagement.data.LibraryDataFactory
import com.example.librarymanagement.data.JournalDataFactory
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

//@DataJpaTest
//@ActiveProfiles(profiles = ["test", "jpa"])
//@Import(JpaLibraryRepository::class)
class JpaLibraryRepositoryTest {
    private val libraryRepositorySpring: LibraryRepositorySpring = mockk()
    private val jpaLibraryRepository = JpaLibraryRepository(libraryRepositorySpring)
    private val jpaLibrary = LibraryDataFactory.createJpaLibrary()

    private val jpaId = JournalDataFactory.JPA_ID
    private val id = jpaId.toString()

    private val library = LibraryDataFactory.createLibrary(jpaId)

    @Test
    fun `should find all libraries`() {
        //GIVEN
        val expected = listOf(library)

        every { libraryRepositorySpring.findAll() } returns listOf(jpaLibrary)

        //WHEN
        val actual = jpaLibraryRepository.findAll()

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should save library`() {
        //GIVEN
        val expected = library

        every { libraryRepositorySpring.save(any()) } returns jpaLibrary

        //WHEN
        val actual = jpaLibraryRepository.save(expected)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify (exactly = 1) { libraryRepositorySpring.save(any()) }
    }

    @Test
    fun `should find library by id`() {
        //GIVEN
        val expected = library

        every { libraryRepositorySpring.findById(jpaId) } returns Optional.of(jpaLibrary)

        //WHEN
        val actual = jpaLibraryRepository.findById(id)

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should delete library by id`() {
        //GIVEN
        every { libraryRepositorySpring.deleteById(jpaId) } returns Unit

        //WHEN
        jpaLibraryRepository.deleteById(id)

        //THEN
        verify { libraryRepositorySpring.deleteById(jpaId) }
    }
}
