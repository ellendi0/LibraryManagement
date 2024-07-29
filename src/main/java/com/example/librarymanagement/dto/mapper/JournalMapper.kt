package com.example.librarymanagement.dto.mapper

import com.example.librarymanagement.dto.JournalDto
import com.example.librarymanagement.model.entity.Journal
import org.springframework.stereotype.Component

@Component
class JournalMapper {
    fun toJournalDto(journal: Journal) : JournalDto {
        val book = journal.bookPresence?.book
        val author = book?.author!!
        val user = journal.user
        val library = journal.bookPresence!!.library

        return JournalDto(
            id = journal.id!!,
            dateOfBorrowing = journal.dateOfBorrowing,
            dateOfReturning = journal.dateOfReturning,
            title = book.title,
            author = "${author.firstName} ${author.lastName}",
            user = "${user?.firstName} ${user?.lastName}",
            nameOfLibrary = library.name
        )
    }

    fun toJournalDto(journals: List<Journal>): List<JournalDto> = journals.map { toJournalDto(it) }
}