package com.example.librarymanagement.data

import com.example.librarymanagement.model.entity.BookPresence
import com.example.librarymanagement.model.entity.Journal
import com.example.librarymanagement.model.entity.User
import java.time.LocalDate

object JournalDataFactory {
    fun createJournal() = Journal(
        id = 1L,
        dateOfBorrowing = LocalDate.now(),
        dateOfReturning = LocalDate.now().plusDays(7)
    )

    fun Journal.withUserAndBookPresence(
        user: User = UserDataFactory.createUser(),
        bookPresence: BookPresence = BookPresenceDataFactory.createBookPresence()
    ): Journal {
        return copy(user = user, bookPresence = bookPresence)
    }
}
