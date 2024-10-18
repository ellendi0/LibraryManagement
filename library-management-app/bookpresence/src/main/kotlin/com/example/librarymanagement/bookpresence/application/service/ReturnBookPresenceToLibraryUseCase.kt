package com.example.librarymanagement.bookpresence.application.service

import com.example.librarymanagement.book.application.port.`in`.ExistsBookByIdInPort
import com.example.librarymanagement.bookpresence.application.port.`in`.ReturnBookPresenceToLibraryInPort
import com.example.librarymanagement.bookpresence.application.port.out.BookPresenceRepositoryOutPort
import com.example.librarymanagement.bookpresence.domain.Availability
import com.example.librarymanagement.core.application.exception.EntityNotFoundException
import com.example.librarymanagement.journal.application.port.`in`.CreateJournalInPort
import com.example.librarymanagement.journal.application.port.`in`.FindAllJournalsByUserIdInPort
import com.example.librarymanagement.journal.application.port.`in`.GetOpenJournalByBookPresenceIdAndUserIdInPort
import com.example.librarymanagement.journal.domain.Journal
import com.example.librarymanagement.library.application.port.`in`.ExistsLibraryByIdInPort
import com.example.librarymanagement.notification.application.port.`in`.NotifyUserAboutBookAvailabilityInPort
import com.example.librarymanagement.notification.infrastructure.nats.NotifyOnAvailabilityNatsPublisher
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import com.example.librarymanagement.user.application.port.`in`.ExistsUserByIdInPort
import java.time.LocalDate

@Service
class ReturnBookPresenceToLibraryUseCase(
    private val bookPresenceRepository: BookPresenceRepositoryOutPort,
    private val getOpenJournalByBookPresenceAndUserIdInPort: GetOpenJournalByBookPresenceIdAndUserIdInPort,
    private val createJournalInPort: CreateJournalInPort,
    private val notifyUserAboutBookAvailabilityInPort: NotifyUserAboutBookAvailabilityInPort,
    private val notifyOnAvailabilityNatsPublisher: NotifyOnAvailabilityNatsPublisher,
    private val findAllJournalsByUserIdInPort: FindAllJournalsByUserIdInPort,
    private val existsUserByIdInPort: ExistsUserByIdInPort,
    private val existsBookByIdInPort: ExistsBookByIdInPort,
    private val existsLibraryByIdInPort: ExistsLibraryByIdInPort
) : ReturnBookPresenceToLibraryInPort {
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
                getOpenJournalByBookPresenceAndUserIdInPort.getOpenJournalByBookPresenceIdAndUserId(
                    presence.id!!,
                    presence.userId!!
                )
                    .zipWith(Mono.just(presence))
                    .onErrorResume { Mono.error(EntityNotFoundException("Journal")) }
            }
            .flatMap {
                it.t1.dateOfReturning = LocalDate.now()
                createJournalInPort.createJournal(it.t1).then(Mono.just(it.t2))
            }
            .flatMap { presence ->
                presence.availability = Availability.AVAILABLE
                presence.userId = null
                bookPresenceRepository.saveOrUpdate(presence)
            }
            .flatMap { notifyUserAboutBookAvailabilityInPort.notifyUserAboutBookAvailability(bookId, libraryId) }
            .flatMap { notifyOnAvailabilityNatsPublisher.publish(bookId, libraryId, it) }
            .flatMapMany { findAllJournalsByUserIdInPort.findAllJournalsByUserId(userId) }
    }

    private fun validate(userId: String, libraryId: String, bookId: String): Mono<Boolean> {
        return Mono.zip(
            existsUserByIdInPort.existsUserById(userId),
            existsLibraryByIdInPort.existsLibraryById(libraryId),
            existsBookByIdInPort.existsBookById(bookId)
        ).map { true }
    }
}
