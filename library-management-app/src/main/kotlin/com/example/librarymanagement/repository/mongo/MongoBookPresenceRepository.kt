package com.example.librarymanagement.repository.mongo

import com.example.librarymanagement.model.domain.BookPresence
import com.example.librarymanagement.model.domain.Journal
import com.example.librarymanagement.model.enums.Availability
import com.example.librarymanagement.model.mongo.MongoBookPresence
import com.example.librarymanagement.repository.BookPresenceRepository
import com.example.librarymanagement.repository.mongo.mapper.MongoBookPresenceMapper
import org.bson.types.ObjectId
import org.springframework.context.annotation.Profile
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.findAndRemove
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate

@Repository
@Profile("mongo")
class MongoBookPresenceRepository(
    private val reactiveMongoTemplate: ReactiveMongoTemplate,
    private val journalRepository: MongoJournalRepository
) : BookPresenceRepository {
    private fun BookPresence.toEntity() = MongoBookPresenceMapper.toEntity(this)
    private fun MongoBookPresence.toDomain() = MongoBookPresenceMapper.toDomain(this)

    override fun saveOrUpdate(bookPresence: BookPresence): Mono<BookPresence> =
        reactiveMongoTemplate.save(bookPresence.toEntity()).map { it.toDomain() }

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
        val results = reactiveMongoTemplate.find(query, MongoBookPresence::class.java)
        return results.map { it.toDomain() }
    }

    override fun findAllByLibraryId(libraryId: String): Flux<BookPresence> {
        val query = Query(Criteria.where(MongoBookPresence::libraryId.name).`is`(ObjectId(libraryId)))
        val results = reactiveMongoTemplate.find(query, MongoBookPresence::class.java)
        return results.map { it.toDomain() }
    }

    override fun findAllByLibraryIdAndBookId(libraryId: String, bookId: String): Flux<BookPresence> {
        val criteria = Criteria
            .where(MongoBookPresence::libraryId.name).`is`(ObjectId(libraryId))
            .and(MongoBookPresence::bookId.name).`is`(ObjectId(bookId))
        val query = Query(criteria)
        val results = reactiveMongoTemplate.find(query, MongoBookPresence::class.java)
        return results.map { it.toDomain() }
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
        val results = reactiveMongoTemplate.find(query, MongoBookPresence::class.java)
        return results.map { it.toDomain() }
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
