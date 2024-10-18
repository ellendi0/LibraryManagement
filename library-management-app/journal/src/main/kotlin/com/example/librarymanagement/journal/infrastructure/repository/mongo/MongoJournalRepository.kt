package com.example.librarymanagement.journal.infrastructure.repository.mongo

import com.example.librarymanagement.journal.application.port.out.JournalRepositoryOutPort
import com.example.librarymanagement.journal.domain.Journal
import com.example.librarymanagement.journal.infrastructure.convertor.JournalMapper
import com.example.librarymanagement.journal.infrastructure.entity.MongoJournal
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class MongoJournalRepository(
    private val reactiveMongoTemplate: ReactiveMongoTemplate,
    private val journalMapper: JournalMapper
) : JournalRepositoryOutPort {
    private fun Journal.toEntity() = journalMapper.toEntity(this)
    private fun MongoJournal.toDomain() = journalMapper.toDomain(this)

    override fun saveOrUpdate(journal: Journal): Mono<Journal> {
        val result: Mono<MongoJournal> = reactiveMongoTemplate.save(journal.toEntity())
        return result.map { it.toDomain() }
    }

    override fun findById(journalId: String): Mono<Journal> {
        val result: Mono<MongoJournal> = reactiveMongoTemplate.findById(ObjectId(journalId), MongoJournal::class.java)
        return result.map { it.toDomain() }
    }

    override fun deleteById(journalId: String): Mono<Unit> {
        val query = Query(Criteria.where("_id").`is`(ObjectId(journalId)))
        return reactiveMongoTemplate.findAndRemove(query, MongoJournal::class.java).then(Mono.just(Unit))
    }

    override fun findAllByUserId(userId: String): Flux<Journal> {
        val result: Flux<MongoJournal> = reactiveMongoTemplate.find(
            Query(Criteria.where(MongoJournal::userId.name).`is`(ObjectId(userId))),
            MongoJournal::class.java
        )
        return result.map { it.toDomain() }
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

        val result: Mono<MongoJournal> = reactiveMongoTemplate.findOne(query, MongoJournal::class.java)
        return result.map { it.toDomain() }
    }
}
