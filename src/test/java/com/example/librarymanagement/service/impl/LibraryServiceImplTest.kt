package com.example.librarymanagement.service.impl

import com.example.librarymanagement.data.TestDataFactory
import com.example.librarymanagement.repository.LibraryRepository
import com.example.librarymanagement.service.BookPresenceService
import com.example.librarymanagement.service.ReservationService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

class LibraryServiceImplTest {
    private val libraryRepository: LibraryRepository = mockk()
    private val bookPresenceService: BookPresenceService = mockk()
    private val reservationService: ReservationService = mockk()
    private val libraryService = LibraryServiceImpl(
        libraryRepository,
        bookPresenceService,
        reservationService
    )

    private val testDataRel = TestDataFactory.createTestDataRelForServices()
    private val library = testDataRel.library

    @Test
    fun shouldCreateLibrary() {
        //GIVEN
        val expected = library

        every { libraryRepository.save(library) } returns library

        //WHEN
        val actual = libraryService.createLibrary(library)

        //THEN
        Assertions.assertEquals(expected, library)
        verify(exactly = 1) { libraryRepository.save(library) }
    }

    @Test
    fun shouldUpdateLibrary() {
        //GIVEN
        val expected = library.copy(name = "Updated")

        every { libraryRepository.findById(1) } returns Optional.of(library)
        every { libraryRepository.save(expected) } returns expected

        //WHEN
        val actual = libraryService.updateLibrary(expected)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { libraryRepository.findById(1) }
        verify(exactly = 1) { libraryRepository.save(expected) }
    }

    @Test
    fun shouldDeleteLibrary() {
        //GIVEN
        every { libraryRepository.findById(1) } returns Optional.of(library)
        every { bookPresenceService.deleteBookPresenceById(1) } returns Unit
        every { reservationService.getReservationsByLibraryId(1) } returns listOf(testDataRel.reservation)
        every { reservationService.deleteReservationById(1) } returns Unit
        every { libraryRepository.deleteById(1) } returns Unit

        //WHEN
        libraryService.deleteLibrary(1)

        //THEN
        verify(exactly = 1) { libraryRepository.findById(1) }
        verify(exactly = 1) { libraryRepository.deleteById(1) }
    }

    @Test
    fun shouldGetLibraryById() {
        //GIVEN
        val expected = library
        every { libraryRepository.findById(1) } returns Optional.of(library)

        //WHEN
        val actual = libraryService.getLibraryById(1)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify (exactly = 1) { libraryRepository.findById(1) }
    }

    @Test
    fun shouldFindAll() {
        //GIVEN
        val excepted = listOf(library)
        every { libraryRepository.findAll() } returns listOf(library)

        //WHEN
        val result = libraryService.findAll()

        //THEN
        Assertions.assertEquals(excepted, result)
        verify(exactly = 1) { libraryRepository.findAll() }
    }
}
