package com.example.librarymanagement.service.impl

import com.example.librarymanagement.exception.EntityNotFoundException
import com.example.librarymanagement.exception.ExistingReservationException
import com.example.librarymanagement.model.domain.Reservation
import com.example.librarymanagement.model.domain.ReservationOutcome
import com.example.librarymanagement.model.enums.Availability
import com.example.librarymanagement.repository.ReservationRepository
import com.example.librarymanagement.service.BookPresenceService
import com.example.librarymanagement.service.JournalService
import com.example.librarymanagement.service.LibraryService
import com.example.librarymanagement.service.ReservationService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class ReservationServiceImpl(
    private val reservationRepository: ReservationRepository,
    private val bookPresenceService: BookPresenceService,
    private val libraryService: LibraryService,
    private val journalService: JournalService
) : ReservationService {

    override fun getReservationsByUserId(userId: String): List<Reservation> =
        reservationRepository.findAllByUserId(userId)

    @Transactional
    override fun reserveBook(userId: String, libraryId: String?, bookId: String): ReservationOutcome {
        if (reservationRepository.existsByBookIdAndUserId(bookId, userId)) {
            throw ExistingReservationException(bookId, userId)
        }

        val libraryForReservation = determineLibraryForReservation(libraryId, bookId)

        val availableBookPresence = bookPresenceService
            .getAllBookPresencesByLibraryIdAndBookId(libraryForReservation, bookId)
            .firstOrNull { it.availability == Availability.AVAILABLE }

        return availableBookPresence?.let {
            bookPresenceService.addUserToBook(userId, availableBookPresence.libraryId, bookId)
            ReservationOutcome.Journals(journalService.getJournalByUserId(userId))
        } ?: run {
            reservationRepository.save(Reservation(userId = userId, libraryId = libraryForReservation, bookId = bookId))
            ReservationOutcome.Reservations(getReservationsByUserId(userId))
        }
    }

    @Transactional
    override fun cancelReservation(userId: String, bookId: String) {
        reservationRepository.findAllByBookIdAndUserId(bookId, userId)
            .map { it.id?.let { it1 -> reservationRepository.deleteById(it1) } }
    }

    override fun deleteReservationById(id: String) {
        reservationRepository.findById(id)?.let { reservationRepository.deleteById(id) }
    }

    private fun determineLibraryForReservation(libraryId: String?, bookId: String): String {
        return libraryId?.let {
            libraryService.getLibraryById(libraryId)
            if (!bookPresenceService.existsBookPresenceByBookIdAndLibraryId(bookId, libraryId)) {
                throw EntityNotFoundException("Presence of book")
            }
            libraryId
        } ?: run {
            findLibraryWithFewestReservations(bookId) ?: throw EntityNotFoundException("Libraries with book")
        }
    }

    private fun findLibraryWithFewestReservations(bookId: String): String? {
        return bookPresenceService.getAllByBookId(bookId)
            .map { bookPresence ->
                val reservationCount = reservationRepository.findAllByBookIdAndLibraryId(
                    bookPresence.bookId,
                    bookPresence.libraryId
                ).size
                bookPresence.libraryId to reservationCount
            }
            .minByOrNull { it.second }
            ?.first
    }
}
