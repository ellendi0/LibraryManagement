package com.example.librarymanagement.service.impl

import com.example.librarymanagement.data.TestDataFactory
import com.example.librarymanagement.exception.ExistingReservationException
import com.example.librarymanagement.model.enums.Availability
import com.example.librarymanagement.repository.BookRepository
import com.example.librarymanagement.repository.ReservationRepository
import com.example.librarymanagement.service.BookPresenceService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

class ReservationServiceImplTest {
    private val reservationRepository: ReservationRepository = mockk()
    private val bookRepository: BookRepository = mockk()
    private val bookPresenceService: BookPresenceService = mockk()

    private val reservationService = ReservationServiceImpl(
        reservationRepository,
        bookRepository,
        bookPresenceService
    )

    private val reservation = TestDataFactory.createTestDataRelForServices().reservation

    @Test
    fun shouldGetReservationByLibraryId() {
        every { reservationRepository.findAllByLibraryId(1) } returns listOf(reservation)

        Assertions.assertEquals(listOf(reservation), reservationService.getReservationsByLibraryId(1))
        verify(exactly = 1) { reservationRepository.findAllByLibraryId(1) }
    }

    @Test
    fun shouldGetReservationByUserId() {
        every { reservationRepository.findAllByUserId(1) } returns listOf(reservation)

        Assertions.assertEquals(listOf(reservation), reservationService.getReservationsByUserId(1))
        verify(exactly = 1) { reservationRepository.findAllByUserId(1) }
    }

    @Test
    fun shouldGetReservationByBookIdAndUser() {
        val user  = TestDataFactory.createTestDataRelForServices().user
        every { reservationRepository.findAllByBookIdAndUserId(1, 1) } returns listOf(reservation)

        Assertions.assertEquals(listOf(reservation), reservationService.getReservationsByBookIdAndUser(1, user))
        verify(exactly = 1) { reservationRepository.findAllByBookIdAndUserId(1, 1) }
    }

    @Test
    fun shouldBorrowIfBookIsAvailable() {
        val user = TestDataFactory.createTestDataRelForServices().user
        val bookPresence = TestDataFactory.createTestDataRelForServices().bookPresence
        val book = TestDataFactory.createTestDataRelForServices().book

        every { bookRepository.findById(1) } returns Optional.of(book)
        every { reservationRepository.existsByBookIdAndUser(1, user) } returns false
        every { bookPresenceService.getAllBookByLibraryIdAndBookId(1, 1) } returns listOf(bookPresence)
        every { bookPresenceService.addUserToBook(user, 1, 1) } returns bookPresence
        every { reservationRepository.save(reservation) } returns reservation
        every { reservationRepository.findAllByUserId(1) } returns listOf(reservation)

        Assertions.assertEquals(listOf(reservation), reservationService.reserveBook(user, 1, 1))
    }

    @Test
    fun shouldReserveIfBookIsUnavailable() {
        val user = TestDataFactory.createTestDataRelForServices().user
        val book = TestDataFactory.createTestDataRelForServices().book
        val bookPresence = TestDataFactory.createTestDataRelForServices()
            .bookPresence.copy(availability = Availability.UNAVAILABLE)

        every { bookRepository.findById(1) } returns Optional.of(book)
        every { reservationRepository.existsByBookIdAndUser(1, user) } returns false
        every { bookPresenceService.getAllBookByLibraryIdAndBookId(1, 1) } returns listOf(bookPresence)
        every { reservationRepository.save(any()) } returns reservation
        every { reservationRepository.findAllByUserId(1) } returns listOf(reservation)

        Assertions.assertEquals(listOf(reservation), reservationService.reserveBook(user, 1, 1))
    }

    @Test
    fun shouldNotReserveBook() {
        val user = TestDataFactory.createTestDataRelForServices().user
        val book = TestDataFactory.createTestDataRelForServices().book

        every { bookRepository.findById(1) } returns Optional.of(book)
        every { reservationRepository.existsByBookIdAndUser(1, user) } returns true

        Assertions.assertThrows(ExistingReservationException::class.java) {
            reservationService.reserveBook(user, 1, 1)
        }
    }

    @Test
    fun shouldRemoveReservation() {
        every { reservationRepository
            .findAllByBookIdAndUserId(1, TestDataFactory.createTestDataRelForServices().user.id!!) }
            .returns(listOf(reservation))
        every { reservationRepository.deleteAll(listOf(reservation)) } returns Unit

        reservationService.removeReservation(TestDataFactory.createTestDataRelForServices().user, 1)

        verify(exactly = 1) { reservationRepository.deleteAll(listOf(reservation)) }
    }

    @Test
    fun  shouldDeleteReservationById() {
        every { reservationRepository.findById(1) } returns Optional.of(reservation)
        every { reservationRepository.delete(reservation) } returns Unit

        reservationService.deleteReservationById(1)

        verify(exactly = 1) { reservationRepository.delete(reservation) }
    }
}
