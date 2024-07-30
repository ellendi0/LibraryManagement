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
        every { libraryRepository.save(library) } returns library

        Assertions.assertEquals(library, libraryService.createLibrary(library))
        verify(exactly = 1) { libraryRepository.save(library) }
    }

    @Test
    fun shouldUpdateLibrary() {
        val updatedLibrary = library.copy(name = "Updated")
        every { libraryRepository.findById(1) } returns Optional.of(library)
        every { libraryRepository.save(updatedLibrary) } returns updatedLibrary

        Assertions.assertEquals(updatedLibrary, libraryService.updateLibrary(1, updatedLibrary))
        verify(exactly = 1) { libraryRepository.findById(1) }
        verify(exactly = 1) { libraryRepository.save(updatedLibrary) }
    }

    @Test
    fun shouldDeleteLibrary() {
        every { libraryRepository.findById(1) } returns Optional.of(library)
        every { bookPresenceService.deleteBookPresenceById(1) } returns Unit
        every { reservationService.getReservationsByLibraryId(1) } returns listOf(testDataRel.reservation)
        every { reservationService.deleteReservationById(1) } returns Unit
        every { libraryRepository.deleteById(1) } returns Unit

        libraryService.deleteLibrary(1)
        verify(exactly = 1) { libraryRepository.findById(1) }
        verify(exactly = 1) { libraryRepository.deleteById(1) }
    }

    @Test
    fun shouldGetLibraryById() {
        every { libraryRepository.findById(1) } returns Optional.of(library)

        Assertions.assertEquals(library, libraryService.getLibraryById(1))
        verify (exactly = 1) { libraryRepository.findById(1) }
    }

    @Test
    fun shouldFindAll() {
        every { libraryRepository.findAll() } returns listOf(library)

        Assertions.assertEquals(listOf(library), libraryService.findAll())
        verify(exactly = 1) { libraryRepository.findAll() }
    }
}