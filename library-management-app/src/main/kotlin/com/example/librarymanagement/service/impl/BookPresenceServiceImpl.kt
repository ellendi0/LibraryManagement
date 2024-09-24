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
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate

@Service
class BookPresenceServiceImpl(
    private val bookPresenceRepository: BookPresenceRepository,
    private val journalService: JournalService,
    private val reservationRepository: ReservationRepository,
    private val bookService: BookService,
    private val libraryService: LibraryService,
    private val userService: UserService,
) : BookPresenceService {

    override fun addUserToBook(userId: String, libraryId: String, bookId: String): Flux<Journal> {
        return validate(userId, libraryId, bookId)
            .flatMap {
                reservationRepository.findFirstByBookIdAndLibraryId(bookId, libraryId)
                    .flatMap { reservation ->
                        when {
                            reservation.userId == userId -> reservationRepository.deleteById(reservation.id!!)
                                .thenReturn(Mono.just(reservation))

                            else -> Mono.error(BookNotAvailableException(libraryId, bookId))
                        }
                    }.switchIfEmpty(Mono.empty())
            }
            .then(bookPresenceRepository.addBookToUser(userId, libraryId, bookId))
            .flatMapMany { journalService.getJournalByUserId(userId) }
            .switchIfEmpty(Mono.error(BookNotAvailableException(libraryId, bookId)))
    }

    override fun addBookToLibrary(libraryId: String, bookId: String): Mono<BookPresence> {
        return Mono.zip(
            libraryService.existsLibraryById(libraryId),
            bookService.existsBookById(bookId)
        )
            .flatMap {
                val bookPresence = BookPresence(bookId = bookId, libraryId = libraryId)
                bookPresenceRepository.saveOrUpdate(bookPresence)
            }
    }

    override fun removeUserFromBook(userId: String, libraryId: String, bookId: String): Flux<Journal> {
        return validate(userId, libraryId, bookId)
            .flatMap {
                bookPresenceRepository.findAllByLibraryIdAndBookIdAndAvailability(
                    libraryId,
                    bookId,
                    Availability.UNAVAILABLE
                )
                    .filter { it.userId == userId }
                    .next()
            }
            .flatMap { presence ->
                journalService.findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(presence.id!!, presence.userId!!)
                    .zipWith(Mono.just(presence))
            }
            .flatMap {
                it.t1.dateOfReturning = LocalDate.now()
                journalService.save(it.t1).then(Mono.just(it.t2))
            }
            .flatMap { presence ->
                presence.availability = Availability.AVAILABLE
                presence.userId = null
                bookPresenceRepository.saveOrUpdate(presence)
            }
            .flatMapMany { journalService.getJournalByUserId(userId) }
            .switchIfEmpty(Mono.error(EntityNotFoundException("Journal")))
    }

    override fun getAllByBookId(bookId: String): Flux<BookPresence> = bookPresenceRepository.findAllByBookId(bookId)

    override fun getAllByLibraryId(libraryId: String): Flux<BookPresence> =
        bookPresenceRepository.findAllByLibraryId(libraryId)

    override fun getAllBookPresencesByLibraryIdAndBookId(libraryId: String, bookId: String): Flux<BookPresence> =
        bookPresenceRepository.findAllByLibraryIdAndBookId(libraryId, bookId)

    override fun deleteBookPresenceById(id: String): Mono<Unit> = bookPresenceRepository.deleteById(id)

    override fun existsBookPresenceByBookIdAndLibraryId(bookId: String, libraryId: String): Mono<Boolean> =
        bookPresenceRepository.existsByBookIdAndLibraryId(bookId, libraryId)

    private fun validate(userId: String, libraryId: String, bookId: String): Mono<Boolean> {
        return Mono.zip(
            userService.existsUserById(userId),
            libraryService.existsLibraryById(libraryId),
            bookService.existsBookById(bookId)
        ).map { true }
    }
}
