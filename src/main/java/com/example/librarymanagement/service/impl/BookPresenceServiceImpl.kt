package com.example.librarymanagement.service.impl

import com.example.librarymanagement.exception.BookNotAvailableException
import com.example.librarymanagement.exception.EntityNotFoundException
import com.example.librarymanagement.model.domain.BookPresence
import com.example.librarymanagement.model.domain.Journal
import com.example.librarymanagement.model.enums.Availability
import com.example.librarymanagement.repository.BookPresenceRepository
import com.example.librarymanagement.repository.ReservationRepository
import com.example.librarymanagement.service.BookPresenceService
import com.example.librarymanagement.service.BookService
import com.example.librarymanagement.service.JournalService
import com.example.librarymanagement.service.LibraryService
import com.example.librarymanagement.service.UserService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class BookPresenceServiceImpl(
    private val bookPresenceRepository: BookPresenceRepository,
    private val journalService: JournalService,
    private val reservationRepository: ReservationRepository,
    private val bookService: BookService,
    private val libraryService: LibraryService,
    private val userService: UserService,
) : BookPresenceService {

    @Transactional
    override fun addUserToBook(userId: String, libraryId: String, bookId: String): List<Journal> {
        val user = userService.getUserById(userId)
        libraryService.getLibraryById(libraryId)

        val reservation = reservationRepository.findFirstByBookIdAndLibraryIdOrLibraryIsNull(bookId, libraryId)

        reservation?.let {
            when {
                it.userId == userId -> it.id?.let { it1 -> reservationRepository.deleteById(it1) }
                else -> throw BookNotAvailableException(libraryId, bookId)
            }
        }

        bookPresenceRepository.addBookToUser(user, libraryId, bookId)
            ?: throw BookNotAvailableException(libraryId, bookId)
        return journalService.getJournalByUserId(userId)
    }

    override fun addBookToLibrary(libraryId: String, bookId: String): BookPresence {
        bookService.getBookById(bookId)
        libraryService.getLibraryById(libraryId)

        val bookPresence = BookPresence(bookId = bookId, libraryId = libraryId)
        return bookPresenceRepository.save(bookPresence)
    }

    override fun removeUserFromBook(userId: String, libraryId: String, bookId: String): List<Journal> {
        val user = userService.getUserById(userId)
        libraryService.getLibraryById(libraryId)

        bookPresenceRepository.removeBookFromUser(user, libraryId, bookId) ?: EntityNotFoundException("Journal")
        return journalService.getJournalByUserId(userId)
    }

    override fun getAllByBookId(bookId: String): List<BookPresence> = bookPresenceRepository.findAllByBookId(bookId)

    override fun getAllByLibraryId(libraryId: String): List<BookPresence> {
        return bookPresenceRepository.findAllByLibraryId(libraryId)
    }

    override fun getAllBookPresencesByLibraryIdAndBookId(libraryId: String, bookId: String): List<BookPresence> {
        return bookPresenceRepository.findAllByLibraryIdAndBookId(libraryId, bookId)
    }

    override fun getAllBookPresencesByLibraryIdAndAvailability(
        libraryId: String,
        availability: Availability
    ): List<BookPresence> {
        return bookPresenceRepository.findAllByLibraryIdAndAvailability(libraryId, availability)
    }

    override fun deleteBookPresenceById(id: String) = bookPresenceRepository.deleteById(id)

    override fun existsBookPresenceByBookIdAndLibraryId(bookId: String, libraryId: String): Boolean {
        return bookPresenceRepository.existsByBookIdAndLibraryId(bookId, libraryId)
    }
}
