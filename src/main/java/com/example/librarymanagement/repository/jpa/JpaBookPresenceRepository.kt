package com.example.librarymanagement.repository.jpa

import com.example.librarymanagement.model.domain.BookPresence
import com.example.librarymanagement.model.enums.Availability
import com.example.librarymanagement.model.jpa.JpaBookPresence
import com.example.librarymanagement.model.jpa.JpaJournal
import com.example.librarymanagement.repository.BookPresenceRepository
import com.example.librarymanagement.repository.jpa.mapper.JpaBookMapper
import com.example.librarymanagement.repository.jpa.mapper.JpaBookPresenceMapper
import com.example.librarymanagement.repository.jpa.mapper.JpaLibraryMapper
import com.example.librarymanagement.repository.jpa.mapper.JpaUserMapper
import org.springframework.context.annotation.Profile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.kotlin.core.publisher.switchIfEmpty
import java.time.LocalDate

@Repository
@Profile("jpa")
class JpaBookPresenceRepository(
    private val bookPresenceRepository: BookPresenceRepositorySpring,
    private val libraryRepository: JpaLibraryRepository,
    private val userRepository: JpaUserRepository,
    private val bookRepository: JpaBookRepository,
    private val journalRepository: JpaJournalRepository,
) : BookPresenceRepository {
    private fun BookPresence.toEntity() = JpaBookPresenceMapper.toEntity(this)
    private fun JpaBookPresence.toDomain() = JpaBookPresenceMapper.toDomain(this)

    override fun saveOrUpdate(bookPresence: BookPresence): Mono<BookPresence> {
        return update(bookPresence)
            .switchIfEmpty { save(bookPresence) }
    }

    override fun addBookToUser(userId: String, libraryId: String, bookId: String): Mono<BookPresence> {
        return Mono.zip(
            jpaFindAllByLibraryIdAndBookIdAndAvailability(libraryId, bookId, Availability.AVAILABLE).next(),
            userRepository.findById(userId)
        )
            .flatMap {
                val bookPresence = it.t1
                val user = JpaUserMapper.toEntity(it.t2)

                val journal = JpaJournal(user = user, bookPresence = bookPresence, dateOfBorrowing = LocalDate.now())
                bookPresence.journals.add(journal)

                Mono.fromCallable { journalRepository.save(journal) }.then(Mono.just(Pair(bookPresence, user)))
            }
            .flatMap {
                val jpaBookPresence = it.first
                val user = it.second

                jpaBookPresence.user = user
                jpaBookPresence.availability = Availability.UNAVAILABLE

                Mono.fromCallable { bookPresenceRepository.save(jpaBookPresence).toDomain() }
                    .subscribeOn(Schedulers.boundedElastic())
            }
    }

    override fun deleteById(bookPresenceId: String): Mono<Unit> =
        Mono.fromCallable { bookPresenceRepository.deleteById(bookPresenceId.toLong()) }
            .subscribeOn(Schedulers.boundedElastic())
            .then(Mono.just(Unit))

    override fun findAllByBookId(bookId: String): Flux<BookPresence> =
        Mono.fromCallable { bookPresenceRepository.findAllByBookId(bookId.toLong()) }
            .subscribeOn(Schedulers.boundedElastic())
            .flatMapMany { Flux.fromIterable(it.map { bookPresence -> bookPresence.toDomain() }) }

    override fun findAllByLibraryId(libraryId: String): Flux<BookPresence> =
        Mono.fromCallable { bookPresenceRepository.findAllByLibraryId(libraryId.toLong()) }
            .subscribeOn(Schedulers.boundedElastic())
            .flatMapMany { Flux.fromIterable(it.map { bookPresence -> bookPresence.toDomain() }) }

    override fun findAllByLibraryIdAndBookId(libraryId: String, bookId: String): Flux<BookPresence> =
        Mono.fromCallable { bookPresenceRepository.findAllByLibraryIdAndBookId(libraryId.toLong(), bookId.toLong()) }
            .subscribeOn(Schedulers.boundedElastic())
            .flatMapMany { Flux.fromIterable(it.map { bookPresence -> bookPresence.toDomain() }) }

    override fun findAllByLibraryIdAndBookIdAndAvailability(
        libraryId: String,
        bookId: String,
        availability: Availability
    ): Flux<BookPresence> {
        return Mono.fromCallable {
            bookPresenceRepository.findAllByLibraryIdAndBookIdAndAvailability(
                libraryId.toLong(),
                bookId.toLong(),
                availability
            )
        }
            .subscribeOn(Schedulers.boundedElastic())
            .flatMapMany { Flux.fromIterable(it.map { bookPresence -> bookPresence.toDomain() }) }
    }

    override fun existsByBookIdAndLibraryId(bookId: String, libraryId: String): Mono<Boolean> =
        Mono.fromCallable { bookPresenceRepository.existsByBookIdAndLibraryId(bookId.toLong(), libraryId.toLong()) }
            .subscribeOn(Schedulers.boundedElastic())

    private fun jpaFindAllByLibraryIdAndBookIdAndAvailability(
        libraryId: String,
        bookId: String,
        availability: Availability
    ): Flux<JpaBookPresence> {
        return Mono.fromCallable {
            bookPresenceRepository.findAllByLibraryIdAndBookIdAndAvailability(
                libraryId.toLong(),
                bookId.toLong(),
                availability
            )
        }
            .subscribeOn(Schedulers.boundedElastic())
            .flatMapMany { Flux.fromIterable(it) }
    }

    private fun findById(id: String): Mono<JpaBookPresence> =
        Mono.fromCallable { bookPresenceRepository.findByIdOrNull(id.toLong()) }

    private fun save(bookPresence: BookPresence): Mono<BookPresence> {
        return Mono.zip(
            libraryRepository.findById(bookPresence.libraryId),
            bookRepository.findById(bookPresence.bookId)
        )
            .flatMap {
                val bookPresenceEntity = bookPresence.toEntity()
                    .copy(library = JpaLibraryMapper.toEntity(it.t1), book = JpaBookMapper.toEntity(it.t2))
                Mono.fromCallable { bookPresenceRepository.save(bookPresenceEntity) }
                    .subscribeOn(Schedulers.boundedElastic())
            }
            .map { it.toDomain() }
    }

    private fun update(bookPresence: BookPresence): Mono<BookPresence> {
        return Mono.zip(
            bookPresence.userId?.let { userRepository.findById(it) } ?: Mono.empty(),
            bookPresence.id?.let { findById(it) } ?: Mono.empty()
        )
            .flatMap {
                val entity = it.t2
                entity.user = JpaUserMapper.toEntity(it.t1)
                Mono.fromCallable { bookPresenceRepository.save(entity).toDomain() }
                    .subscribeOn(Schedulers.boundedElastic())
            }
            .switchIfEmpty(Mono.empty())
    }
}

@Repository
interface BookPresenceRepositorySpring : JpaRepository<JpaBookPresence, Long> {
    fun findAllByBookId(bookId: Long): List<JpaBookPresence>
    fun findAllByLibraryId(libraryId: Long): List<JpaBookPresence>
    fun findAllByUserId(userId: Long): List<JpaBookPresence>
    fun findAllByLibraryIdAndBookId(libraryId: Long, bookId: Long): List<JpaBookPresence>
    fun findAllByLibraryIdAndBookIdAndAvailability(
        libraryId: Long,
        bookId: Long,
        availability: Availability
    ): List<JpaBookPresence>

    fun existsByBookIdAndLibraryId(bookId: Long, libraryId: Long): Boolean
}
