package com.example.librarymanagement.repository.jpa

import com.example.librarymanagement.exception.EntityNotFoundException
import com.example.librarymanagement.exception.ExistingReservationException
import com.example.librarymanagement.model.domain.Reservation
import com.example.librarymanagement.model.domain.User
import com.example.librarymanagement.model.enums.Availability
import com.example.librarymanagement.model.jpa.JpaReservation
import com.example.librarymanagement.model.jpa.JpaUser
import com.example.librarymanagement.repository.ReservationRepository
import com.example.librarymanagement.repository.jpa.mapper.JpaBookMapper
import com.example.librarymanagement.repository.jpa.mapper.JpaLibraryMapper
import com.example.librarymanagement.repository.jpa.mapper.JpaReservationMapper
import com.example.librarymanagement.repository.jpa.mapper.JpaUserMapper
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class JpaReservationRepository(
        private val reservationRepository: ReservationRepositorySpring,
        private val bookRepository: BookRepositorySpring,
        private val bookPresenceRepository: JpaBookPresenceRepository
) : ReservationRepository{
    private fun Reservation.toEntity() = JpaReservationMapper.toEntity(this)
    private fun JpaReservation.toDomain() = JpaReservationMapper.toDomain(this)

    override fun save(reservation: Reservation): Reservation {
        return reservationRepository.save(reservation.toEntity()).toDomain()
    }

    override fun findById(reservationId: String): Reservation? {
        return reservationRepository.findByIdOrNull(reservationId.toLong())?.toDomain()
    }

    override fun deleteById(reservationId: String) {
        return reservationRepository.deleteById(reservationId.toLong())
    }

    override fun delete(reservation: Reservation) {
        return reservationRepository.delete(reservation.toEntity())
    }

    @Transactional
    override fun reserveBook(user: User, libraryId: String?, bookId: String): List<Reservation> {
        val book = JpaBookMapper.toDomain(
            bookRepository.findByIdOrNull(bookId.toLong())
                ?: throw EntityNotFoundException("Book"))

        if (existsByBookIdAndUser(bookId, user)) {
            throw ExistingReservationException(bookId, user.id!!)
        }

        val bookPresenceList = libraryId
            ?.let {
                bookPresenceRepository.findAllByLibraryIdAndAvailability(it, Availability.AVAILABLE)
            } ?: bookPresenceRepository.findAllByBookId(bookId)

        val bookPresence = bookPresenceList.firstOrNull { it.availability == Availability.AVAILABLE }

        bookPresence?.library?.id
            ?.let {
                bookPresenceRepository.addUserToBook(user, it, bookId)
            } ?: run {
                val reservation = JpaReservation(
                    book = JpaBookMapper.toEntity(book),
                    user = JpaUserMapper.toEntity(user),
                    library = bookPresenceList.firstOrNull()?.library?.let { JpaLibraryMapper.toEntity(it) })

            reservationRepository.save(reservation)
        }

        return findAllByUserId(user.id!!)
    }

    override fun findAllByBookIdAndUserId(bookId: String, userId: String): List<Reservation> {
        return reservationRepository.findAllByBookIdAndUserId(bookId.toLong(), userId.toLong()).map { it.toDomain() }
    }

    override fun findAllByLibraryId(libraryId: String): List<Reservation> {
        return reservationRepository.findAllByLibraryId(libraryId.toLong()).map { it.toDomain() }
    }

    override fun findAllByUserId(userId: String): List<Reservation> {
        return reservationRepository.findAllByUserId(userId.toLong()).map { it.toDomain() }
    }

    override fun findFirstByBookIdAndLibraryIdOrLibraryIsNull(bookId: String, libraryId: String?): Reservation? {
        return reservationRepository.findFirstByBookIdAndLibraryIdOrLibraryIsNull(bookId.toLong(), libraryId?.toLong())
                ?.toDomain()
    }

    override fun existsByBookIdAndUser(bookId: String, user: User): Boolean {
        return reservationRepository.existsByBookIdAndUser(bookId.toLong(), JpaUserMapper.toEntity(user))
    }
}

@Repository
interface ReservationRepositorySpring : JpaRepository<JpaReservation, Long> {
    fun findAllByBookIdAndUserId(bookId: Long, userId: Long): List<JpaReservation>
    fun findAllByLibraryId(libraryId: Long): List<JpaReservation>
    fun findAllByUserId(userId: Long): List<JpaReservation>
    fun findFirstByBookIdAndLibraryIdOrLibraryIsNull(bookId: Long, libraryId: Long?): JpaReservation?
    fun existsByBookIdAndUser(bookId: Long, user: JpaUser): Boolean
}
