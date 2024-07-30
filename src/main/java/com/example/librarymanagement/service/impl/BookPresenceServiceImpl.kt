package com.example.librarymanagement.service.impl

import com.example.librarymanagement.exception.BookNotAvailableException
import com.example.librarymanagement.exception.EntityNotFoundException
import com.example.librarymanagement.model.entity.BookPresence
import com.example.librarymanagement.model.entity.Journal
import com.example.librarymanagement.model.entity.User
import com.example.librarymanagement.model.enums.Availability
import com.example.librarymanagement.repository.BookPresenceRepository
import com.example.librarymanagement.repository.BookRepository
import com.example.librarymanagement.repository.LibraryRepository
import com.example.librarymanagement.repository.ReservationRepository
import com.example.librarymanagement.service.BookPresenceService
import com.example.librarymanagement.service.JournalService
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class BookPresenceServiceImpl(
    private val bookPresenceRepository: BookPresenceRepository,
    private val journalService: JournalService,
    private val reservationRepository: ReservationRepository,
    private val bookRepository: BookRepository,
    private val libraryRepository: LibraryRepository
) : BookPresenceService {

    override fun createBookPresence(bookPresence: BookPresence): BookPresence {
        return bookPresenceRepository.save(bookPresence)
    }

    override fun updateBookPresence(id:Long, bookPresence: BookPresence): BookPresence {
        val bookPresenceToUpdate = bookPresenceRepository.findByIdOrNull(id) ?: throw EntityNotFoundException("Presence")
        bookPresenceToUpdate.availability = bookPresence.availability
        return bookPresenceRepository.save(bookPresenceToUpdate)
    }

    @Transactional
    override fun addUserToBook(user: User, libraryId: Long, bookId: Long): BookPresence {
        val bookPresence = findAllByLibraryIdAndBookIdAndAvailability(libraryId, bookId, Availability.AVAILABLE)
            .firstOrNull() ?: throw BookNotAvailableException(libraryId, bookId)

        val journal = Journal(user = user, bookPresence = bookPresence, dateOfBorrowing = LocalDate.now())

        bookPresence.journals.add(journal)
        journalService.createJournal(journal)

        bookPresence.user = user
        bookPresence.availability = Availability.UNAVAILABLE

        return bookPresenceRepository.save(bookPresence)
    }

    override fun addBookToLibrary(libraryId: Long, bookId: Long): BookPresence {
        val book = bookRepository.findByIdOrNull(bookId) ?: throw EntityNotFoundException("Book")
        val library = libraryRepository.findByIdOrNull(libraryId) ?: throw EntityNotFoundException("Library")

        val bookPresence = BookPresence(book = book, library = library)
        return bookPresenceRepository.save(bookPresence)
    }

    @Transactional
    override fun removeUserFromBook(user: User, libraryId: Long, bookId: Long): BookPresence {
        val bookPresence = findAllByLibraryIdAndBookIdAndAvailability(libraryId, bookId, Availability.UNAVAILABLE)
            .firstOrNull() ?: throw BookNotAvailableException(libraryId, bookId)

        val journal = journalService.findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(bookPresence.id!!, user.id!!)
        journal.dateOfReturning = LocalDate.now()

        journalService.createJournal(journal)
        updateBookPresence(bookPresence.id!!, bookPresence.apply { availability = Availability.AVAILABLE })

        val reservation = reservationRepository.findFirstByBookIdAndLibraryIdOrLibraryIsNull(bookId, libraryId)

        reservation?.let {
            addUserToBook(it.user!!, libraryId, bookId)
            reservationRepository.delete(reservation)
        } ?: run {
            bookPresence.availability = Availability.AVAILABLE
            bookPresence.user = null
        }
        return bookPresenceRepository.save(bookPresence)
    }

    override fun getByBookId(bookId: Long): List<BookPresence> = bookPresenceRepository.findAllByBookId(bookId)

    override fun getByLibraryId(libraryId: Long): List<BookPresence> {
        return bookPresenceRepository.findAllByLibraryId(libraryId)
    }

    override fun getAllBookByLibraryIdAndBookId(libraryId: Long, bookId: Long): List<BookPresence> {
        return bookPresenceRepository.findAllByLibraryIdAndBookId(libraryId, bookId)
    }

    override fun getAllBookByLibraryIdAndAvailability(libraryId: Long, availability: Availability): List<BookPresence> {
        return bookPresenceRepository.findAllByLibraryIdAndAvailability(libraryId, availability)
    }

    override fun findAllByLibraryIdAndBookIdAndAvailability(libraryId: Long, bookId: Long, availability: Availability): List<BookPresence> {
        return bookPresenceRepository.findAllByLibraryIdAndBookIdAndAvailability(libraryId, bookId, availability)
    }

    override fun deleteBookPresenceByIdAndLibraryId(libraryId: Long, bookId: Long) {
        bookPresenceRepository.deleteBookPresenceByIdAndLibraryId(bookId, libraryId)
    }

    override fun deleteBookPresenceById(id: Long) = bookPresenceRepository.deleteById(id)
}