package com.example.librarymanagement.repository.jpa

import com.example.librarymanagement.exception.BookNotAvailableException
import com.example.librarymanagement.exception.EntityNotFoundException
import com.example.librarymanagement.model.domain.BookPresence
import com.example.librarymanagement.model.domain.User
import com.example.librarymanagement.model.enums.Availability
import com.example.librarymanagement.model.jpa.JpaBookPresence
import com.example.librarymanagement.model.jpa.JpaJournal
import com.example.librarymanagement.repository.BookPresenceRepository
import com.example.librarymanagement.repository.jpa.mapper.JpaBookPresenceMapper
import com.example.librarymanagement.repository.jpa.mapper.JpaUserMapper
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class JpaBookPresenceRepository(
    private val jpaBookPresenceRepository: BookPresenceRepositorySpring,
    private val jpaReservationRepository: ReservationRepositorySpring,
    private val jpaJournalRepository: JournalRepositorySpring,
    private val jpaBookRepository: JpaBookRepository,
    private val jpaLibraryRepository: JpaLibraryRepository,
): BookPresenceRepository {
    private fun BookPresence.toEntity() = JpaBookPresenceMapper.toEntity(this)
    private fun JpaBookPresence.toDomain() = JpaBookPresenceMapper.toDomain(this)

    override fun save(bookPresence: BookPresence): BookPresence {
        return jpaBookPresenceRepository.save(bookPresence.toEntity()).toDomain()
    }

    override fun deleteById(bookPresenceId: String) {
        return jpaBookPresenceRepository.deleteById(bookPresenceId.toLong())
    }

    @Transactional
    override fun addUserToBook(user: User, libraryId: String, bookId: String): BookPresence {
        val bookPresence = jpaBookPresenceRepository.findAllByLibraryIdAndBookIdAndAvailability(
                libraryId.toLong(),
                bookId.toLong(),
                Availability.AVAILABLE)
                .firstOrNull() ?: throw BookNotAvailableException(libraryId, bookId)

        val jpaUser = JpaUserMapper.toEntity(user)
        val reservation = jpaReservationRepository.findFirstByBookIdAndLibraryIdOrLibraryIsNull(
            bookId.toLong(),
            libraryId.toLong())

        reservation?.let {
            when {
                reservation.user.id == user.id?.toLong() -> jpaReservationRepository.delete(reservation)
                else -> throw BookNotAvailableException(libraryId, bookId)
            }
        }

        val journal = JpaJournal(user = jpaUser, bookPresence = bookPresence, dateOfBorrowing = LocalDate.now())

        bookPresence.journals.add(journal)
        jpaJournalRepository.save(journal)

        bookPresence.user = jpaUser
        bookPresence.availability = Availability.UNAVAILABLE

        return jpaBookPresenceRepository.save(bookPresence).toDomain()
    }

    override fun addBookToLibrary(libraryId: String, bookId: String): BookPresence {
        val book = jpaBookRepository.findById(bookId) ?: throw EntityNotFoundException("Book")
        val library = jpaLibraryRepository.findById(libraryId) ?: throw EntityNotFoundException("Library")

        val bookPresence = BookPresence(book = book, library = library)
        return save(bookPresence)
    }

    @Transactional
    override fun removeUserFromBook(user: User, libraryId: String, bookId: String): BookPresence {
        val bookPresence = jpaBookPresenceRepository.findAllByLibraryIdAndBookIdAndAvailability(
                libraryId.toLong(),
                bookId.toLong(),
                Availability.UNAVAILABLE)
                .firstOrNull() ?: throw BookNotAvailableException(libraryId, bookId)

        val journal = jpaJournalRepository.findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(
                bookPresence.id!!,
                user.id!!.toLong())
        journal?.dateOfReturning = LocalDate.now()
        journal?.let { jpaJournalRepository.save(it) }

        bookPresence.availability = Availability.AVAILABLE
        bookPresence.user = null

        return jpaBookPresenceRepository.save(bookPresence).toDomain()
    }

    override fun findAllByBookId(bookId: String): List<BookPresence> {
        return jpaBookPresenceRepository.findAllByBookId(bookId.toLong()).map { it.toDomain() }
    }

    override fun findAllByLibraryId(libraryId: String): List<BookPresence> {
        return jpaBookPresenceRepository.findAllByLibraryId(libraryId.toLong()).map { it.toDomain() }
    }

    override fun findAllByUserId(userId: String): List<BookPresence> {
        return jpaBookPresenceRepository.findAllByUserId(userId.toLong()).map { it.toDomain() }
    }

    override fun findAllByLibraryIdAndBookId(libraryId: String, bookId: String): List<BookPresence> {
        return jpaBookPresenceRepository.findAllByLibraryIdAndBookId(libraryId.toLong(), bookId.toLong())
                .map { it.toDomain() }
    }

    override fun findAllByLibraryIdAndBookIdAndAvailability(
            libraryId: String,
            bookId: String,
            availability: Availability
    ): List<BookPresence> {
        return jpaBookPresenceRepository.findAllByLibraryIdAndBookIdAndAvailability(
                libraryId.toLong(),
                bookId.toLong(),
                availability)
                .map { it.toDomain() }
    }

    override fun findAllByLibraryIdAndAvailability(
            libraryId: String,
            availability: Availability): List<BookPresence> {
        return jpaBookPresenceRepository.findAllByLibraryIdAndAvailability(libraryId.toLong(), availability)
                .map { it.toDomain() }
    }
}

@Repository
interface BookPresenceRepositorySpring: JpaRepository<JpaBookPresence, Long>{
    fun findAllByBookId(bookId: Long): List<JpaBookPresence>
    fun findAllByLibraryId(libraryId: Long): List<JpaBookPresence>
    fun findAllByUserId(userId: Long): List<JpaBookPresence>
    fun findAllByLibraryIdAndBookId(libraryId: Long, bookId: Long): List<JpaBookPresence>
    fun findAllByLibraryIdAndBookIdAndAvailability(
            libraryId: Long,
            bookId: Long,
            availability: Availability): List<JpaBookPresence>
    fun findAllByLibraryIdAndAvailability(libraryId: Long, availability: Availability): List<JpaBookPresence>
}
