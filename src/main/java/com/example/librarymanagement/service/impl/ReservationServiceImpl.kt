package com.example.librarymanagement.service.impl

import com.example.librarymanagement.exception.EntityNotFoundException
import com.example.librarymanagement.exception.ExistingReservationException
import com.example.librarymanagement.model.entity.Reservation
import com.example.librarymanagement.model.entity.User
import com.example.librarymanagement.model.enums.Availability
import com.example.librarymanagement.repository.BookRepository
import com.example.librarymanagement.repository.ReservationRepository
import com.example.librarymanagement.service.BookPresenceService
import com.example.librarymanagement.service.ReservationService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class ReservationServiceImpl(
    private val reservationRepository: ReservationRepository,
    private val bookRepository: BookRepository,
    private val bookPresenceService: BookPresenceService
) : ReservationService {

    override fun getReservationsByLibraryId(libraryId: Long): List<Reservation> {
        return reservationRepository.findAllByLibraryId(libraryId)
    }

    override fun getReservationsByUserId(userId: Long): List<Reservation> = reservationRepository.findAllByUserId(userId)

    override fun getReservationsByBookIdAndUser(bookId: Long, user: User): List<Reservation> {
        return reservationRepository.findAllByBookIdAndUserId(bookId, user.id!!)
    }

    @Transactional
    override fun reserveBook(user: User, libraryId: Long?, bookId: Long): List<Reservation> {
        val book = bookRepository.findById(bookId).orElseThrow { throw EntityNotFoundException("Book") }
        if (reservationRepository.existsByBookIdAndUser(bookId, user)) { throw ExistingReservationException(bookId, user.id!!) }

        val bookPresenceList = libraryId?.let {
            bookPresenceService.getAllBookByLibraryIdAndAvailability(it, Availability.AVAILABLE)
        } ?: bookPresenceService.getByBookId(bookId)

        val bookPresence = bookPresenceList.firstOrNull { it.availability == Availability.AVAILABLE }

        bookPresence?.library?.id?.let {
            bookPresenceService.addUserToBook(user, it, bookId)
        } ?: run {
            val reservation = Reservation(book = book, user = user, library = bookPresenceList.firstOrNull()?.library)
            reservationRepository.save(reservation)
        }
        return getReservationsByUserId(user.id!!)
    }

    @Transactional
    override fun removeReservation(user: User, bookId: Long) {
        reservationRepository.deleteAll(getReservationsByBookIdAndUser(bookId, user))
    }

    override fun deleteReservationById(id: Long) {
        reservationRepository.findById(id).ifPresent { reservationRepository.delete(it) }
    }
}