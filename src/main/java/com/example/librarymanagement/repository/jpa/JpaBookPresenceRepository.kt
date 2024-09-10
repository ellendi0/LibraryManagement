package com.example.librarymanagement.repository.jpa

import com.example.librarymanagement.model.domain.BookPresence
import com.example.librarymanagement.model.domain.User
import com.example.librarymanagement.model.enums.Availability
import com.example.librarymanagement.model.jpa.JpaBookPresence
import com.example.librarymanagement.model.jpa.JpaJournal
import com.example.librarymanagement.repository.BookPresenceRepository
import com.example.librarymanagement.repository.jpa.mapper.JpaBookPresenceMapper
import com.example.librarymanagement.repository.jpa.mapper.JpaUserMapper
import jakarta.transaction.Transactional
import org.springframework.context.annotation.Profile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
@Profile("jpa")
class JpaBookPresenceRepository(
    private val bookPresenceRepository: BookPresenceRepositorySpring,
    private val journalRepository: JpaJournalRepository,
) : BookPresenceRepository {
    private fun BookPresence.toEntity() = JpaBookPresenceMapper.toEntity(this)
    private fun JpaBookPresence.toDomain() = JpaBookPresenceMapper.toDomain(this)

    override fun save(bookPresence: BookPresence): BookPresence {
        return bookPresenceRepository.save(bookPresence.toEntity()).toDomain()
    }

    override fun addBookToUser(user: User, libraryId: String, bookId: String): BookPresence? {
        val jpaBookPresence = bookPresenceRepository
            .findAllByLibraryIdAndBookIdAndAvailability(
                libraryId.toLong(),
                bookId.toLong(),
                Availability.AVAILABLE
            )
            .firstOrNull() ?: return null

        val jpaUser = JpaUserMapper.toEntity(user)

        val journal = JpaJournal(
            user = jpaUser,
            bookPresence = jpaBookPresence,
            dateOfBorrowing = LocalDate.now()
        )

        jpaBookPresence.journals.add(journal)
        journalRepository.save(journal)

        jpaBookPresence.user = jpaUser
        jpaBookPresence.availability = Availability.UNAVAILABLE

        return bookPresenceRepository.save(jpaBookPresence).toDomain()
    }

    @Transactional
    override fun removeBookFromUser(user: User, libraryId: String, bookId: String): BookPresence? {
        val jpaBookPresence = bookPresenceRepository
            .findAllByLibraryIdAndBookIdAndAvailability(
                libraryId.toLong(),
                bookId.toLong(),
                Availability.UNAVAILABLE
            )
            .firstOrNull { it.user?.id == user.id?.toLong() }
            ?: return null

        val journal = journalRepository.findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(
            jpaBookPresence.id!!,
            jpaBookPresence.user?.id!!
        )
            ?: return null

        journal.dateOfReturning = LocalDate.now()

        journalRepository.save(journal)

        jpaBookPresence.availability = Availability.AVAILABLE
        jpaBookPresence.user = null

        return bookPresenceRepository.save(jpaBookPresence).toDomain()
    }

    override fun findById(bookPresenceId: String): BookPresence? {
        return bookPresenceRepository.findByIdOrNull(bookPresenceId.toLong())?.toDomain()
    }

    override fun deleteById(bookPresenceId: String) {
        return bookPresenceRepository.deleteById(bookPresenceId.toLong())
    }

    override fun findAllByBookId(bookId: String): List<BookPresence> {
        return bookPresenceRepository.findAllByBookId(bookId.toLong()).map { it.toDomain() }
    }

    override fun findAllByLibraryId(libraryId: String): List<BookPresence> {
        return bookPresenceRepository.findAllByLibraryId(libraryId.toLong()).map { it.toDomain() }
    }

    override fun findAllByUserId(userId: String): List<BookPresence> {
        return bookPresenceRepository.findAllByUserId(userId.toLong()).map { it.toDomain() }
    }

    override fun findAllByLibraryIdAndBookId(libraryId: String, bookId: String): List<BookPresence> {
        return bookPresenceRepository.findAllByLibraryIdAndBookId(libraryId.toLong(), bookId.toLong())
            .map { it.toDomain() }
    }

    override fun findAllByLibraryIdAndBookIdAndAvailability(
        libraryId: String,
        bookId: String,
        availability: Availability
    ): List<BookPresence> {
        return bookPresenceRepository.findAllByLibraryIdAndBookIdAndAvailability(
            libraryId.toLong(),
            bookId.toLong(),
            availability
        )
            .map { it.toDomain() }
    }

    override fun findAllByLibraryIdAndAvailability(
        libraryId: String,
        availability: Availability
    ): List<BookPresence> {
        return bookPresenceRepository.findAllByLibraryIdAndAvailability(libraryId.toLong(), availability)
            .map { it.toDomain() }
    }

    override fun existsByBookIdAndLibraryId(bookId: String, libraryId: String): Boolean {
        return bookPresenceRepository.existsByBookIdAndLibraryId(bookId.toLong(), libraryId.toLong())
    }
}

@Repository
interface BookPresenceRepositorySpring : JpaRepository<JpaBookPresence, Long> {
    fun findAllByBookId(bookId: Long): List<JpaBookPresence>
    fun findAllByLibraryId(libraryId: Long): List<JpaBookPresence>
    fun findAllByUserId(userId: Long): List<JpaBookPresence>
    fun findAllByLibraryIdAndBookId(libraryId: Long, bookId: Long): List<JpaBookPresence>
    fun findAllByLibraryIdAndBookIdAndAvailability(
        libraryId: Long,
        bookId: Long,
        availability: Availability
    ): List<JpaBookPresence>

    fun findAllByLibraryIdAndAvailability(libraryId: Long, availability: Availability): List<JpaBookPresence>
    fun existsByBookIdAndLibraryId(bookId: Long, libraryId: Long): Boolean
}
