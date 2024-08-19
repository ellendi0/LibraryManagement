package com.example.librarymanagement.service.impl

import com.example.librarymanagement.exception.EntityNotFoundException
import com.example.librarymanagement.model.domain.BookPresence
import com.example.librarymanagement.model.domain.User
import com.example.librarymanagement.model.enums.Availability
import com.example.librarymanagement.repository.BookPresenceRepository
import com.example.librarymanagement.service.BookPresenceService
import com.example.librarymanagement.service.JournalService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class BookPresenceServiceImpl(
    private val bookPresenceRepository: BookPresenceRepository,
) : BookPresenceService {

    override fun addUserToBook(user: User, libraryId: String, bookId: String): BookPresence {
        return bookPresenceRepository.addUserToBook(user, libraryId, bookId)
    }

    override fun addBookToLibrary(libraryId: String, bookId: String): BookPresence {
        return bookPresenceRepository.addBookToLibrary(libraryId, bookId)
    }

    @Transactional
    override fun removeUserFromBook(user: User, libraryId: String, bookId: String): BookPresence {
        return bookPresenceRepository.removeUserFromBook(user, libraryId, bookId)
    }

    override fun getByBookId(bookId: String): List<BookPresence> = bookPresenceRepository.findAllByBookId(bookId)

    override fun getByLibraryId(libraryId: String): List<BookPresence> {
        return bookPresenceRepository.findAllByLibraryId(libraryId)
    }

    override fun getAllBookByLibraryIdAndBookId(libraryId: String, bookId: String): List<BookPresence> {
        return bookPresenceRepository.findAllByLibraryIdAndBookId(libraryId, bookId)
    }

    override fun getAllBookByLibraryIdAndAvailability(
            libraryId: String,
            availability: Availability
    ): List<BookPresence> {
        return bookPresenceRepository.findAllByLibraryIdAndAvailability(libraryId, availability)
    }

    override fun deleteBookPresenceById(id: String) = bookPresenceRepository.deleteById(id)
}
