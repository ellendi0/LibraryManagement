package com.example.librarymanagement.repository.jpa

import com.example.librarymanagement.model.domain.Journal
import com.example.librarymanagement.model.jpa.JpaJournal
import com.example.librarymanagement.repository.JournalRepository
import com.example.librarymanagement.repository.jpa.mapper.JpaJournalMapper
import org.springframework.context.annotation.Profile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.kotlin.core.publisher.switchIfEmpty

@Repository
@Profile("jpa")
class JpaJournalRepository(
    private val journalRepository: JournalRepositorySpring,
) : JournalRepository {
    private fun Journal.toEntity() = JpaJournalMapper.toEntity(this)
    private fun JpaJournal.toDomain() = JpaJournalMapper.toDomain(this)

    override fun saveOrUpdate(journal: Journal): Mono<Journal> =
        update(journal)
            .switchIfEmpty { save(journal.toEntity()) }

    override fun findById(journalId: String): Mono<Journal> =
        Mono.fromCallable { journalRepository.findByIdOrNull(journalId.toLong())?.toDomain() }

    override fun deleteById(journalId: String): Mono<Unit> =
        Mono.fromCallable { journalRepository.deleteById(journalId.toLong()) }
            .subscribeOn(Schedulers.boundedElastic())
            .then(Mono.just(Unit))

    override fun findAllByUserId(userId: String): Flux<Journal> =
        Mono.fromCallable { journalRepository.findAllByUserId(userId.toLong()) }
            .subscribeOn(Schedulers.boundedElastic())
            .flatMapMany { Flux.fromIterable(it.map { journal -> journal.toDomain() }) }

    override fun findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(
        bookPresenceId: String,
        userId: String
    ): Mono<Journal> {
        return Mono.fromCallable {
            journalRepository.findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(
                bookPresenceId.toLong(),
                userId.toLong()
            )
        }
            .subscribeOn(Schedulers.boundedElastic())
            .flatMap { jpaJournal ->
                if (jpaJournal != null) {
                    Mono.just(jpaJournal.toDomain())
                } else {
                    Mono.empty()
                }
            }
    }

    fun save(jpaJournal: JpaJournal): Mono<Journal> {
        return Mono.fromCallable { journalRepository.save(jpaJournal) }
            .subscribeOn(Schedulers.boundedElastic())
            .map { it.toDomain() }
    }

    fun update(journal: Journal): Mono<Journal> {
        return Mono.defer {
            journalRepository.findByIdOrNull(journal.id!!.toLong())
                ?.let { jpa ->
                    jpa.dateOfReturning = journal.dateOfReturning
                    Mono.fromCallable { journalRepository.save(jpa) }
                        .subscribeOn(Schedulers.boundedElastic())
                        .map { it.toDomain() }
                } ?: Mono.empty()
        }
    }
}

@Repository
interface JournalRepositorySpring : JpaRepository<JpaJournal, Long> {
    fun findAllByUserId(userId: Long): List<JpaJournal>
    fun findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(bookPresenceId: Long, userId: Long): JpaJournal?
}
