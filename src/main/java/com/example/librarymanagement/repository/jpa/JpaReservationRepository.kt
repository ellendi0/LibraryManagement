package com.example.librarymanagement.repository.jpa

import com.example.librarymanagement.model.domain.Reservation
import com.example.librarymanagement.model.jpa.JpaReservation
import com.example.librarymanagement.repository.ReservationRepository
import com.example.librarymanagement.repository.jpa.mapper.JpaBookMapper
import com.example.librarymanagement.repository.jpa.mapper.JpaLibraryMapper
import com.example.librarymanagement.repository.jpa.mapper.JpaReservationMapper
import com.example.librarymanagement.repository.jpa.mapper.JpaUserMapper
import org.springframework.context.annotation.Profile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Repository
@Profile("jpa")
class JpaReservationRepository(
    private val reservationRepository: ReservationRepositorySpring,
    private val userRepository: JpaUserRepository,
    private val bookRepository: JpaBookRepository,
    private val libraryRepository: JpaLibraryRepository
) : ReservationRepository {
    private fun Reservation.toEntity() = JpaReservationMapper.toEntity(this)
    private fun JpaReservation.toDomain() = JpaReservationMapper.toDomain(this)

    override fun save(reservation: Reservation): Mono<Reservation> {
        return Mono.zip(
            userRepository.findById(reservation.userId),
            bookRepository.findById(reservation.bookId),
            libraryRepository.findById(reservation.libraryId)
        )
            .flatMap {
                val reservationEntity = reservation.toEntity()
                    .copy(
                        user = JpaUserMapper.toEntity(it.t1),
                        book = JpaBookMapper.toEntity(it.t2),
                        library = JpaLibraryMapper.toEntity(it.t3)
                    )
                Mono.fromCallable { reservationRepository.save(reservationEntity) }
                    .subscribeOn(Schedulers.boundedElastic())
            }
            .map { it.toDomain() }
    }

    override fun findById(reservationId: String): Mono<Reservation> =
        Mono.fromCallable { reservationRepository.findByIdOrNull(reservationId.toLong())?.toDomain() }

    override fun deleteById(reservationId: String): Mono<Unit> =
        Mono.fromCallable { reservationRepository.deleteById(reservationId.toLong()) }
            .subscribeOn(Schedulers.boundedElastic())
            .then(Mono.just(Unit))

    override fun findAllByBookIdAndUserId(bookId: String, userId: String): Flux<Reservation> =
        Mono.fromCallable { reservationRepository.findAllByBookIdAndUserId(bookId.toLong(), userId.toLong()) }
            .subscribeOn(Schedulers.boundedElastic())
            .flatMapMany { Flux.fromIterable(it.map { reservation -> reservation.toDomain() }) }

    override fun findAllByUserId(userId: String): Flux<Reservation> =
        Mono.fromCallable { reservationRepository.findAllByUserId(userId.toLong()) }
            .subscribeOn(Schedulers.boundedElastic())
            .flatMapMany { Flux.fromIterable(it.map { reservation -> reservation.toDomain() }) }

    override fun findFirstByBookIdAndLibraryId(bookId: String, libraryId: String): Mono<Reservation> =
        Mono.fromCallable { reservationRepository.findFirstByBookIdAndLibraryId(bookId.toLong(), libraryId.toLong()) }
            .subscribeOn(Schedulers.boundedElastic())
            .mapNotNull { it?.toDomain() }

    override fun existsByBookIdAndUserId(bookId: String, userId: String): Mono<Boolean> =
        Mono.fromCallable { reservationRepository.existsByBookIdAndUserId(bookId.toLong(), userId.toLong()) }
            .subscribeOn(Schedulers.boundedElastic())
}

@Repository
interface ReservationRepositorySpring : JpaRepository<JpaReservation, Long> {
    fun findAllByBookIdAndUserId(bookId: Long, userId: Long): List<JpaReservation>
    fun findAllByLibraryId(libraryId: Long): List<JpaReservation>
    fun findAllByUserId(userId: Long): List<JpaReservation>
    fun findAllByBookId(bookId: Long): List<JpaReservation>
    fun findFirstByBookIdAndLibraryId(bookId: Long, libraryId: Long): JpaReservation?
    fun existsByBookIdAndUserId(bookId: Long, userId: Long): Boolean
}
