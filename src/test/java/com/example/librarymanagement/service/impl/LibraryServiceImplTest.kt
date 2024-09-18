package com.example.librarymanagement.service.impl

import com.example.librarymanagement.data.LibraryDataFactory
import com.example.librarymanagement.data.ReservationDataFactory
import com.example.librarymanagement.repository.LibraryRepository
import com.example.librarymanagement.service.BookPresenceService
import com.example.librarymanagement.service.ReservationService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class LibraryServiceImplTest {
    private val libraryRepository: LibraryRepository = mockk()
    private val bookPresenceService: BookPresenceService = mockk()
    private val reservationService: ReservationService = mockk()
    private val libraryService = LibraryServiceImpl(
        libraryRepository
    )
    private val id = "1"

    private val library = LibraryDataFactory.createLibrary(id)

    @Test
    fun shouldCreateLibrary() {
        //GIVEN
        val expected = library

        every { libraryRepository.save(library) } returns library

        //WHEN
        val actual = libraryService.createLibrary(library)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { libraryRepository.save(library) }
    }

    @Test
    fun shouldUpdateLibrary() {
        //GIVEN
        val expected = library.copy(name = "Updated")

        every { libraryRepository.findById(id) } returns library
        every { libraryRepository.save(expected) } returns expected

        //WHEN
        val actual = libraryService.updateLibrary(expected)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { libraryRepository.findById(id) }
        verify(exactly = 1) { libraryRepository.save(expected) }
    }

    @Test
    fun shouldDeleteLibraryById() {
        //GIVEN
        val reservation = ReservationDataFactory.createReservation(id)

        every { libraryRepository.findById(id) } returns library
        every { bookPresenceService.deleteBookPresenceById(id) } returns Unit
        every { reservationService.getReservationsByUserId(id) } returns listOf(reservation)
        every { reservationService.deleteReservationById(id) } returns Unit
        every { libraryRepository.deleteById(id) } returns Unit

        //WHEN
        libraryService.deleteLibraryById(id)

        //THEN
        verify(exactly = 1) { libraryRepository.deleteById(id) }
    }

    @Test
    fun shouldGetLibraryById() {
        //GIVEN
        val expected = library
        every { libraryRepository.findById(id) } returns library

        //WHEN
        val actual = libraryService.getLibraryById(id)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { libraryRepository.findById(id) }
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
