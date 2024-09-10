package com.example.librarymanagement.repository.mongo

import com.example.librarymanagement.model.domain.Journal
import com.example.librarymanagement.model.mongo.MongoJournal
import com.example.librarymanagement.repository.JournalRepository
import com.example.librarymanagement.repository.mongo.mapper.MongoJournalMapper
import org.bson.types.ObjectId
import org.springframework.context.annotation.Profile
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
@Profile("mongo")
class MongoJournalRepository(
    private val mongoTemplate: MongoTemplate
) : JournalRepository {
    private fun Journal.toEntity() = MongoJournalMapper.toEntity(this)
    private fun MongoJournal.toDomain() = MongoJournalMapper.toDomain(this)

    override fun save(journal: Journal): Journal {
        return mongoTemplate.save(journal.toEntity()).toDomain()
    }

    override fun findById(journalId: String): Journal? {
        return mongoTemplate.findById(ObjectId(journalId), MongoJournal::class.java)?.toDomain()
    }

    override fun deleteById(journalId: String) {
        val query = Query(Criteria.where("_id").`is`(ObjectId(journalId)))
        mongoTemplate.findAndRemove(query, MongoJournal::class.java)
    }

    override fun findAllByUserId(userId: String): List<Journal> {
        return mongoTemplate.find(Query(Criteria
            .where(MongoJournal::userId.name).`is`(ObjectId(userId))), MongoJournal::class.java).
        map { it.toDomain() }
    }

    override fun findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(
        bookPresenceId: String,
        userId: String
    ): Journal? {
        val query = Query(Criteria
            .where(MongoJournal::bookPresenceId.name).`is`(ObjectId(bookPresenceId))
            .and(MongoJournal::userId.name).`is`(ObjectId(userId))
            .and(MongoJournal::dateOfReturning.name).`is`(null))
        return mongoTemplate.find(query, MongoJournal::class.java).map { it.toDomain() }.firstOrNull()
    }
}
