package com.example.librarymanagement.bookpresence.application.service

import com.example.librarymanagement.book.application.port.`in`.ExistsBookByIdInPort
import com.example.librarymanagement.bookpresence.application.port.`in`.BorrowBookPresenceFromLibraryInPort
import com.example.librarymanagement.bookpresence.application.port.out.BookPresenceRepositoryOutPort
import com.example.librarymanagement.bookpresence.domain.Availability
import com.example.librarymanagement.core.application.exception.BookAvailabilityException
import com.example.librarymanagement.journal.application.port.`in`.FindAllJournalsByUserIdInPort
import com.example.librarymanagement.journal.domain.Journal
import com.example.librarymanagement.library.application.port.`in`.ExistsLibraryByIdInPort
import com.example.librarymanagement.notification.infrastructure.kafka.KafkaProducerService
import com.example.librarymanagement.reservation.application.port.out.ReservationRepositoryOutPort
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import com.example.librarymanagement.user.application.port.`in`.ExistsUserByIdInPort

@Service
class BorrowBookPresenceFromLibraryUseCase(
    private val bookPresenceRepository: BookPresenceRepositoryOutPort,
    private val reservationRepository: ReservationRepositoryOutPort,
    private val findAllJournalsByUserIdInPort: FindAllJournalsByUserIdInPort,
    private val existsUserByIdInPort: ExistsUserByIdInPort,
    private val existsLibraryByIdInPort: ExistsLibraryByIdInPort,
    private val existsBookByIdInPort: ExistsBookByIdInPort,
    private val kafkaProducerService: KafkaProducerService
) : BorrowBookPresenceFromLibraryInPort {
    override fun borrowBookFromLibrary(userId: String, libraryId: String, bookId: String): Flux<Journal> {
        return validate(userId, libraryId, bookId)
            .flatMap {
                reservationRepository.findFirstByBookIdAndLibraryId(bookId, libraryId)
                    .flatMap { reservation ->
                        when {
                            reservation.userId == userId -> reservationRepository.deleteById(reservation.id!!)
                                .thenReturn(Mono.just(reservation))

                            else -> Mono.error(
                                BookAvailabilityException(
                                    libraryId,
                                    bookId,
                                    Availability.UNAVAILABLE.toString()
                                )
                            )
                        }
                    }
                    .switchIfEmpty(Mono.empty())

                    .flatMap { kafkaProducerService.sendNotification(bookId, libraryId) }
            }
            .then(bookPresenceRepository.addBookToUser(userId, libraryId, bookId))
            .flatMapMany { findAllJournalsByUserIdInPort.findAllJournalsByUserId(userId) }
            .switchIfEmpty(
                Mono.error(
                    BookAvailabilityException(
                        libraryId,
                        bookId,
                        Availability.UNAVAILABLE.toString()
                    )
                )
            )
    }

    private fun validate(userId: String, libraryId: String, bookId: String): Mono<Boolean> {
        return Mono.zip(
            existsUserByIdInPort.existsUserById(userId),
            existsLibraryByIdInPort.existsLibraryById(libraryId),
            existsBookByIdInPort.existsBookById(bookId)
        ).map { true }
    }
}
