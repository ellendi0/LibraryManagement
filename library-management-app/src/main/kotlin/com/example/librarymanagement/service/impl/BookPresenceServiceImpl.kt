package com.example.librarymanagement.service.impl

import com.example.librarymanagement.exception.BookAvailabilityException
import com.example.librarymanagement.exception.EntityNotFoundException
import com.example.librarymanagement.model.domain.BookPresence
import com.example.librarymanagement.model.domain.Journal
import com.example.librarymanagement.model.enums.Availability
import com.example.librarymanagement.publisher.NotifyOnAvailabilityNatsPublisher
import com.example.librarymanagement.repository.BookPresenceRepository
import com.example.librarymanagement.repository.ReservationRepository
import com.example.librarymanagement.service.AvailabilityNotificationService
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
    private val notifyOnAvailabilityNatsPublisher: NotifyOnAvailabilityNatsPublisher,
    private val availabilityNotificationService: AvailabilityNotificationService
) : BookPresenceService {

    override fun borrowBookFromLibrary(userId: String, libraryId: String, bookId: String): Flux<Journal> {
        return validate(userId, libraryId, bookId)
            .flatMap {
                reservationRepository.findFirstByBookIdAndLibraryId(bookId, libraryId)
                    .flatMap { reservation ->
                        when {
                            reservation.userId == userId -> reservationRepository.deleteById(reservation.id!!)
                                .thenReturn(Mono.just(reservation))

                            else -> Mono.error(BookAvailabilityException(libraryId, bookId, Availability.UNAVAILABLE))
                        }
                    }.switchIfEmpty(Mono.empty())
            }
            .then(bookPresenceRepository.addBookToUser(userId, libraryId, bookId))
            .flatMapMany { journalService.getJournalByUserId(userId) }
            .switchIfEmpty(Mono.error(BookAvailabilityException(libraryId, bookId, Availability.UNAVAILABLE)))
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

    override fun returnBookToLibrary(userId: String, libraryId: String, bookId: String): Flux<Journal> {
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
                journalService.getByBookPresenceIdAndUserIdAndDateOfReturningIsNull(presence.id!!, presence.userId!!)
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
            .flatMap { availabilityNotificationService.notifyUserAboutBookAvailability(bookId, libraryId) }
            .flatMap { notifyOnAvailabilityNatsPublisher.publish(bookId, libraryId, it) }
            .flatMapMany { journalService.getJournalByUserId(userId) }
            .switchIfEmpty(Mono.error(EntityNotFoundException("Journal")))
    }

    override fun getAllByBookId(bookId: String): Flux<BookPresence> = bookPresenceRepository.findAllByBookId(bookId)

    override fun getAllByLibraryId(libraryId: String): Flux<BookPresence> =
        bookPresenceRepository.findAllByLibraryId(libraryId)

    override fun getAllBookPresencesByLibraryIdAndBookId(libraryId: String, bookId: String): Flux<BookPresence> =
        bookPresenceRepository.findAllByLibraryIdAndBookId(libraryId, bookId)

    override fun deleteBookPresenceById(id: String): Mono<Unit> = bookPresenceRepository.deleteById(id)

    override fun existsAvailableBookInLibrary(bookId: String, libraryId: String): Mono<Boolean> {
        return getAllBookPresencesByLibraryIdAndBookId(libraryId, bookId)
            .filter { it.availability == Availability.AVAILABLE }
            .hasElements()
    }

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
