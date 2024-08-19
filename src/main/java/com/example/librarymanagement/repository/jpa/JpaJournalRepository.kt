package com.example.librarymanagement.repository.jpa

import com.example.librarymanagement.model.domain.Journal
import com.example.librarymanagement.model.jpa.JpaJournal
import com.example.librarymanagement.repository.JournalRepository
import com.example.librarymanagement.repository.jpa.mapper.JpaJournalMapper
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class JpaJournalRepository(
        private val journalRepository: JournalRepositorySpring
) : JournalRepository {
    private fun Journal.toEntity() = JpaJournalMapper.toEntity(this)
    private fun JpaJournal.toDomain() = JpaJournalMapper.toDomain(this)

    override fun save(journal: Journal): Journal {
        return journalRepository.save(journal.toEntity()).toDomain()
    }

    override fun findById(journalId: String): Journal? {
        return journalRepository.findByIdOrNull(journalId.toLong())?.toDomain()
    }

    override fun deleteById(journalId: String) {
        return journalRepository.deleteById(journalId.toLong())
    }

    override fun findByBookPresenceIdAndUserId(bookPresenceId: String, userId: String): List<Journal> {
        return journalRepository.findByBookPresenceIdAndUserId(bookPresenceId.toLong(), userId.toLong())
                .map { it.toDomain() }
    }

    override fun findAllByUserId(userId: String): List<Journal> {
        return journalRepository.findAllByUserId(userId.toLong()).map { it.toDomain() }
    }

    override fun findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(
            bookPresenceId: String,
            userId: String
    ): Journal? {
        return journalRepository.findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(
                bookPresenceId.toLong(),
                userId.toLong())?.toDomain()
    }
}

@Repository
interface JournalRepositorySpring : JpaRepository<JpaJournal, Long> {
    fun findByBookPresenceIdAndUserId(bookPresenceId: Long, userId: Long): List<JpaJournal>
    fun findAllByUserId(userId: Long): List<JpaJournal>
    fun findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(bookPresenceId: Long, userId: Long): JpaJournal?
}
