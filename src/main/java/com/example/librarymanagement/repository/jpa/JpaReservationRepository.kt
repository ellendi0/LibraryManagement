package com.example.librarymanagement.repository.jpa

import com.example.librarymanagement.model.domain.Reservation
import com.example.librarymanagement.model.jpa.JpaReservation
import com.example.librarymanagement.repository.ReservationRepository
import com.example.librarymanagement.repository.jpa.mapper.JpaBookMapper
import com.example.librarymanagement.repository.jpa.mapper.JpaLibraryMapper
import com.example.librarymanagement.repository.jpa.mapper.JpaReservationMapper
import com.example.librarymanagement.repository.jpa.mapper.JpaUserMapper
import org.springframework.context.annotation.Profile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
@Profile("jpa")
class JpaReservationRepository(
    private val reservationRepository: ReservationRepositorySpring,
    private val userRepository: JpaUserRepository,
    private val bookRepository: JpaBookRepository,
    private val libraryRepository: JpaLibraryRepository,
) : ReservationRepository{
    private fun Reservation.toEntity() = JpaReservationMapper.toEntity(this)
    private fun JpaReservation.toDomain() = JpaReservationMapper.toDomain(this)

    override fun save(reservation: Reservation): Reservation {
        return reservationRepository.save(reservation.toEntity().copy(
            user = JpaUserMapper.toEntity(userRepository.findById(reservation.userId)!!),
            book = JpaBookMapper.toEntity(bookRepository.findById(reservation.bookId)!!),
            library = reservation.libraryId?.let { JpaLibraryMapper.toEntity(libraryRepository.findById(it)!!) }
        )).toDomain()
    }

    override fun findById(reservationId: String): Reservation? {
        return reservationRepository.findByIdOrNull(reservationId.toLong())?.toDomain()
    }

    override fun deleteById(reservationId: String) {
        return reservationRepository.deleteById(reservationId.toLong())
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

    override fun findAllByBookId(bookId: String): List<Reservation> {
        return reservationRepository.findAllByBookId(bookId.toLong()).map { it.toDomain() }
    }

    override fun findFirstByBookIdAndLibraryIdOrLibraryIsNull(bookId: String, libraryId: String?): Reservation? {
        return reservationRepository.findFirstByBookIdAndLibraryIdOrLibraryIsNull(bookId.toLong(), libraryId?.toLong())
                ?.toDomain()
    }

    override fun existsByBookIdAndUserId(bookId: String, userId: String): Boolean {
        return reservationRepository
            .existsByBookIdAndUserId(bookId.toLong(), userId.toLong())
    }

    override fun findAllByBookIdAndLibraryId(bookId: String, libraryId: String): List<Reservation> {
        return reservationRepository
            .findAllByBookIdAndLibraryId(bookId.toLong(), libraryId.toLong()).map { it.toDomain() }
    }
}

@Repository
interface ReservationRepositorySpring : JpaRepository<JpaReservation, Long> {
    fun findAllByBookIdAndUserId(bookId: Long, userId: Long): List<JpaReservation>
    fun findAllByLibraryId(libraryId: Long): List<JpaReservation>
    fun findAllByUserId(userId: Long): List<JpaReservation>
    fun findAllByBookId(bookId: Long): List<JpaReservation>
    fun findFirstByBookIdAndLibraryIdOrLibraryIsNull(bookId: Long, libraryId: Long?): JpaReservation?
    fun existsByBookIdAndUserId(bookId: Long, userId: Long): Boolean
    fun findAllByBookIdAndLibraryId(bookId: Long, libraryId: Long): List<JpaReservation>
}
