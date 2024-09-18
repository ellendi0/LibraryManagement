package com.example.librarymanagement.repository.mongo

import com.example.librarymanagement.model.domain.Journal
import com.example.librarymanagement.model.mongo.MongoJournal
import com.example.librarymanagement.repository.JournalRepository
import com.example.librarymanagement.repository.mongo.mapper.MongoJournalMapper
import org.bson.types.ObjectId
import org.springframework.context.annotation.Profile
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
@Profile("mongo")
class MongoJournalRepository(
    private val reactiveMongoTemplate: ReactiveMongoTemplate,
) : JournalRepository {
    private fun Journal.toEntity() = MongoJournalMapper.toEntity(this)
    private fun MongoJournal.toDomain() = MongoJournalMapper.toDomain(this)

    override fun saveOrUpdate(journal: Journal): Mono<Journal> =
        reactiveMongoTemplate.save(journal.toEntity()).map { it.toDomain() }
//
//    override fun update(journal: Journal): Mono<Journal> = save(journal)

    override fun findById(journalId: String): Mono<Journal> =
        reactiveMongoTemplate.findById(ObjectId(journalId), MongoJournal::class.java).map { it.toDomain() }

    override fun deleteById(journalId: String): Mono<Unit> {
        val query = Query(Criteria.where("_id").`is`(ObjectId(journalId)))
        return reactiveMongoTemplate.findAndRemove(query, MongoJournal::class.java).then(Mono.just(Unit))
    }

    override fun findAllByUserId(userId: String): Flux<Journal> {
        return reactiveMongoTemplate.find(
            Query(Criteria.where(MongoJournal::userId.name).`is`(ObjectId(userId))),
            MongoJournal::class.java
        ).map { it.toDomain() }
    }

    override fun findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(
        bookPresenceId: String,
        userId: String
    ): Mono<Journal> {
        val query = Query(
            Criteria
                .where(MongoJournal::bookPresenceId.name).`is`(ObjectId(bookPresenceId))
                .and(MongoJournal::userId.name).`is`(ObjectId(userId))
                .and(MongoJournal::dateOfReturning.name).`is`(null)
        )
        return reactiveMongoTemplate.findOne(query, MongoJournal::class.java).map { it.toDomain() }
    }
}
