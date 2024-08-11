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
        //GIVEN
        val expected = listOf(reservation)
        every { reservationRepository.findAllByLibraryId(1) } returns listOf(reservation)

        //WHEN
        val actual = reservationService.getReservationsByLibraryId(1)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { reservationRepository.findAllByLibraryId(1) }
    }

    @Test
    fun shouldGetReservationByUserId() {
        //GIVEN
        val expected = listOf(reservation)
        every { reservationRepository.findAllByUserId(1) } returns listOf(reservation)

        //WHEN
        val actual = reservationService.getReservationsByUserId(1)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { reservationRepository.findAllByUserId(1) }
    }

    @Test
    fun shouldGetReservationByBookIdAndUser() {
        //GIVEN
        val expected = listOf(reservation)
        val user  = TestDataFactory.createTestDataRelForServices().user
        every { reservationRepository.findAllByBookIdAndUserId(1, 1) } returns listOf(reservation)

        //WHEN
        val actual = reservationService.getReservationsByBookIdAndUser(1,user)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { reservationRepository.findAllByBookIdAndUserId(1, 1) }
    }

    @Test
    fun shouldBorrowIfBookIsAvailable() {
        //GIVEN
        val expected = listOf(reservation)
        val user = TestDataFactory.createTestDataRelForServices().user
        val bookPresence = TestDataFactory.createTestDataRelForServices().bookPresence
        val book = TestDataFactory.createTestDataRelForServices().book

        every { bookRepository.findById(1) } returns Optional.of(book)
        every { reservationRepository.existsByBookIdAndUser(1, user) } returns false
        every { bookPresenceService.getAllBookByLibraryIdAndBookId(1, 1) } returns listOf(bookPresence)
        every { bookPresenceService.addUserToBook(user, 1, 1) } returns bookPresence
        every { reservationRepository.save(reservation) } returns reservation
        every { reservationRepository.findAllByUserId(1) } returns listOf(reservation)

        //WHEN
        val actual = reservationService.reserveBook(user, 1, 1)

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldReserveIfBookIsUnavailable() {
        //GIVEN
        val expected = listOf(reservation)
        val user = TestDataFactory.createTestDataRelForServices().user
        val book = TestDataFactory.createTestDataRelForServices().book
        val bookPresence = TestDataFactory.createTestDataRelForServices()
            .bookPresence.copy(availability = Availability.UNAVAILABLE)

        every { bookRepository.findById(1) } returns Optional.of(book)
        every { reservationRepository.existsByBookIdAndUser(1, user) } returns false
        every { bookPresenceService.getAllBookByLibraryIdAndBookId(1, 1) } returns listOf(bookPresence)
        every { reservationRepository.save(any()) } returns reservation
        every { reservationRepository.findAllByUserId(1) } returns listOf(reservation)

        //WHEN
        val actual = reservationService.reserveBook(user, 1, 1)

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldNotReserveBook() {
        //GIVEN
        val expected = ExistingReservationException::class.java
        val user = TestDataFactory.createTestDataRelForServices().user
        val book = TestDataFactory.createTestDataRelForServices().book

        every { bookRepository.findById(1) } returns Optional.of(book)
        every { reservationRepository.existsByBookIdAndUser(1, user) } returns true

        //THEN
        Assertions.assertThrows(expected) {
            reservationService.reserveBook(user, 1, 1)
        }
    }

    @Test
    fun shouldRemoveReservation() {
        //GIVEN
        every { reservationRepository
            .findAllByBookIdAndUserId(1, TestDataFactory.createTestDataRelForServices().user.id!!) }
            .returns(listOf(reservation))
        every { reservationRepository.deleteAll(listOf(reservation)) } returns Unit

        //WHEN
        reservationService.removeReservation(TestDataFactory.createTestDataRelForServices().user, 1)

        //THEN
        verify(exactly = 1) { reservationRepository.deleteAll(listOf(reservation)) }
    }

    @Test
    fun  shouldDeleteReservationById() {
        //GIVEN
        every { reservationRepository.findById(1) } returns Optional.of(reservation)
        every { reservationRepository.delete(reservation) } returns Unit

        //WHEN
        reservationService.deleteReservationById(1)

        //THEN
        verify(exactly = 1) { reservationRepository.delete(reservation) }
    }
}
