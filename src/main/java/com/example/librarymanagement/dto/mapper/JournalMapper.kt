package com.example.librarymanagement.dto.mapper

import com.example.librarymanagement.dto.JournalDto
import com.example.librarymanagement.model.domain.Journal
import org.springframework.stereotype.Component

@Component
class JournalMapper {
    fun toJournalDto(journal: Journal) : JournalDto {
        return JournalDto(
            id = journal.id!!,
            dateOfBorrowing = journal.dateOfBorrowing,
            dateOfReturning = journal.dateOfReturning,
            bookPresenceId = journal.bookPresenceId,
            userId = journal.userId
        )
    }

    fun toJournalDto(journals: List<Journal>): List<JournalDto> = journals.map { toJournalDto(it) }
}
