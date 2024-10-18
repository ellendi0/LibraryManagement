package com.example.librarymanagement.bookpresence.infrastructure.repository.mongo

import com.example.librarymanagement.bookpresence.application.port.out.BookPresenceRepositoryOutPort
import com.example.librarymanagement.bookpresence.domain.Availability
import com.example.librarymanagement.bookpresence.domain.BookPresence
import com.example.librarymanagement.bookpresence.infrastructure.convertor.BookPresenceMapper
import com.example.librarymanagement.bookpresence.infrastructure.entity.MongoBookPresence
import com.example.librarymanagement.journal.domain.Journal
import com.example.librarymanagement.journal.infrastructure.repository.mongo.MongoJournalRepository
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.findAndRemove
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate

@Repository
class MongoBookPresenceRepository(
    private val reactiveMongoTemplate: ReactiveMongoTemplate,
    private val journalRepository: MongoJournalRepository,
    private val bookPresenceMapper: BookPresenceMapper
) : BookPresenceRepositoryOutPort {
    private fun BookPresence.toEntity() = bookPresenceMapper.toEntity(this)
    private fun MongoBookPresence.toDomain() = bookPresenceMapper.toDomain(this)

    override fun saveOrUpdate(bookPresence: BookPresence): Mono<BookPresence> {
        val result: Mono<MongoBookPresence> = reactiveMongoTemplate.save(bookPresence.toEntity())
        return result.map { it.toDomain() }
    }

    override fun addBookToUser(userId: String, libraryId: String, bookId: String): Mono<BookPresence> {
        return findAllByLibraryIdAndBookIdAndAvailability(libraryId, bookId, Availability.AVAILABLE)
            .next()
            .flatMap { mongoBookPresence ->
                val journal = Journal(
                    userId = userId,
                    bookPresenceId = mongoBookPresence.id.toString(),
                    dateOfBorrowing = LocalDate.now()
                )
                journalRepository.saveOrUpdate(journal).thenReturn(mongoBookPresence)
            }
            .map { mongoBookPresence ->
                mongoBookPresence.userId = userId
                mongoBookPresence.availability = Availability.UNAVAILABLE
                mongoBookPresence
            }
            .flatMap { saveOrUpdate(it) }
            .switchIfEmpty(Mono.empty())
    }

    override fun deleteById(bookPresenceId: String): Mono<Unit> {
        return reactiveMongoTemplate.findAndRemove<MongoBookPresence>(
            Query(Criteria.where(MongoBookPresence::id.name).`is`(ObjectId(bookPresenceId)))
        ).then(Mono.just(Unit))
    }

    override fun findAllByBookId(bookId: String): Flux<BookPresence> {
        val query = Query(Criteria.where(MongoBookPresence::bookId.name).`is`(ObjectId(bookId)))
        val result: Flux<MongoBookPresence> = reactiveMongoTemplate.find(query, MongoBookPresence::class.java)
        return result.map { it.toDomain() }
    }

    override fun findAllByLibraryId(libraryId: String): Flux<BookPresence> {
        val query = Query(Criteria.where(MongoBookPresence::libraryId.name).`is`(ObjectId(libraryId)))
        val result: Flux<MongoBookPresence> = reactiveMongoTemplate.find(query, MongoBookPresence::class.java)
        return result.map { it.toDomain() }
    }

    override fun findAllByLibraryIdAndBookId(libraryId: String, bookId: String): Flux<BookPresence> {
        val criteria = Criteria
            .where(MongoBookPresence::libraryId.name).`is`(ObjectId(libraryId))
            .and(MongoBookPresence::bookId.name).`is`(ObjectId(bookId))
        val query = Query(criteria)
        val result: Flux<MongoBookPresence> = reactiveMongoTemplate.find(query, MongoBookPresence::class.java)
        return result.map { it.toDomain() }
    }

    override fun findAllByLibraryIdAndBookIdAndAvailability(
        libraryId: String,
        bookId: String,
        availability: Availability
    ): Flux<BookPresence> {
        val criteria = Criteria
            .where(MongoBookPresence::libraryId.name).`is`(ObjectId(libraryId))
            .and(MongoBookPresence::bookId.name).`is`(ObjectId(bookId))
            .and(MongoBookPresence::availability.name).`is`(availability)
        val query = Query(criteria)
        val result: Flux<MongoBookPresence> = reactiveMongoTemplate.find(query, MongoBookPresence::class.java)
        return result.map { it.toDomain() }
    }

    override fun existsByBookIdAndLibraryId(bookId: String, libraryId: String): Mono<Boolean> {
        val query = Query(
            Criteria
                .where(MongoBookPresence::bookId.name).`is`(ObjectId(bookId))
                .and(MongoBookPresence::libraryId.name).`is`(ObjectId(libraryId))
        )

        return reactiveMongoTemplate.exists(query, MongoBookPresence::class.java)
    }
}
