package com.example.librarymanagement.repository.mongo

import com.example.librarymanagement.exception.EntityNotFoundException
import com.example.librarymanagement.model.domain.BookPresence
import com.example.librarymanagement.model.domain.Journal
import com.example.librarymanagement.model.domain.User
import com.example.librarymanagement.model.enums.Availability
import com.example.librarymanagement.model.mongo.MongoBookPresence
import com.example.librarymanagement.repository.BookPresenceRepository
import com.example.librarymanagement.repository.mongo.mapper.MongoBookPresenceMapper
import com.example.librarymanagement.repository.mongo.mapper.MongoUserMapper
import jakarta.transaction.Transactional
import org.bson.types.ObjectId
import org.springframework.context.annotation.Profile
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.findAndRemove
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
@Profile("mongo")
class MongoBookPresenceRepository(
    private val mongoTemplate: MongoTemplate,
    private val journalRepository: MongoJournalRepository
) : BookPresenceRepository {

    private fun BookPresence.toEntity() = MongoBookPresenceMapper.toEntity(this)
    private fun MongoBookPresence.toDomain() = MongoBookPresenceMapper.toDomain(this)

    override fun save(bookPresence: BookPresence): BookPresence {
        return mongoTemplate.save(bookPresence.toEntity()).toDomain()
    }

    @Transactional
    override fun addBookToUser(user: User, libraryId: String, bookId: String): BookPresence? {
        val mongoBookPresence = findAllByLibraryIdAndBookIdAndAvailability(
            libraryId,
            bookId,
            Availability.AVAILABLE
        )
            .firstOrNull()?.toEntity() ?: return null

        val mongoUser = MongoUserMapper.toEntity(user)

        val journal = Journal(
            userId = user.id.toString(),
            bookPresenceId = mongoBookPresence.id.toString(),
            dateOfBorrowing = LocalDate.now()
        )

        journalRepository.save(journal)

        mongoBookPresence.userId = mongoUser.id
        mongoBookPresence.availability = Availability.UNAVAILABLE

        return mongoTemplate.save(mongoBookPresence).toDomain()
    }

    @Transactional
    override fun removeBookFromUser(user: User, libraryId: String, bookId: String): BookPresence? {
        val mongoBookPresence = findAllByLibraryIdAndBookIdAndAvailability(libraryId, bookId, Availability.UNAVAILABLE)
            .firstOrNull { it.userId == user.id } ?: return null

        val journal = journalRepository.findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(
            mongoBookPresence.id!!,
            mongoBookPresence.userId!!
        )
            ?: throw EntityNotFoundException("Journal")

        journal.dateOfReturning = LocalDate.now()

        journalRepository.save(journal)

        mongoBookPresence.availability = Availability.AVAILABLE
        mongoBookPresence.userId = null

        return save(mongoBookPresence)
    }

    override fun findById(bookPresenceId: String): BookPresence? {
        return mongoTemplate.findById(ObjectId(bookPresenceId), MongoBookPresence::class.java)?.toDomain()
    }

    override fun deleteById(bookPresenceId: String) {
        mongoTemplate.findAndRemove<MongoBookPresence>(
            Query(Criteria.where(MongoBookPresence::id.name).`is`(ObjectId(bookPresenceId)))
        )
    }

    override fun findAllByBookId(bookId: String): List<BookPresence> {
        val query = Query(Criteria.where(MongoBookPresence::bookId.name).`is`(ObjectId(bookId)))
        val results = mongoTemplate.find(query, MongoBookPresence::class.java)
        return results.map { it.toDomain() }
    }

    override fun findAllByLibraryId(libraryId: String): List<BookPresence> {
        val query = Query(Criteria.where(MongoBookPresence::libraryId.name).`is`(ObjectId(libraryId)))
        val results = mongoTemplate.find(query, MongoBookPresence::class.java)
        return results.map { it.toDomain() }
    }

    override fun findAllByUserId(userId: String): List<BookPresence> {
        val query = Query(Criteria.where(MongoBookPresence::userId.name).`is`(ObjectId(userId)))
        val results = mongoTemplate.find(query, MongoBookPresence::class.java)
        return results.map { it.toDomain() }
    }

    override fun findAllByLibraryIdAndBookId(libraryId: String, bookId: String): List<BookPresence> {
        val criteria = Criteria
            .where(MongoBookPresence::libraryId.name).`is`(ObjectId(libraryId))
            .and(MongoBookPresence::bookId.name).`is`(ObjectId(bookId))
        val query = Query(criteria)
        val results = mongoTemplate.find(query, MongoBookPresence::class.java)
        return results.map { it.toDomain() }
    }

    override fun findAllByLibraryIdAndBookIdAndAvailability(
        libraryId: String,
        bookId: String,
        availability: Availability
    ): List<BookPresence> {
        val criteria = Criteria
            .where(MongoBookPresence::libraryId.name).`is`(ObjectId(libraryId))
            .and(MongoBookPresence::bookId.name).`is`(ObjectId(bookId))
            .and(MongoBookPresence::availability.name).`is`(availability)
        val query = Query(criteria)
        val results = mongoTemplate.find(query, MongoBookPresence::class.java)
        return results.map { it.toDomain() }
    }

    override fun findAllByLibraryIdAndAvailability(
        libraryId: String,
        availability: Availability
    ): List<BookPresence> {
        val criteria = Criteria
            .where(MongoBookPresence::libraryId.name).`is`(ObjectId(libraryId))
            .and(MongoBookPresence::availability.name).`is`(availability)
        val query = Query(criteria)
        val results = mongoTemplate.find(query, MongoBookPresence::class.java)
        return results.map { it.toDomain() }
    }

    override fun existsByBookIdAndLibraryId(bookId: String, libraryId: String): Boolean {
        val query = Query(
            Criteria
                .where(MongoBookPresence::bookId.name).`is`(ObjectId(bookId))
                .and(MongoBookPresence::libraryId.name).`is`(ObjectId(libraryId))
        )

        return mongoTemplate.exists(query, MongoBookPresence::class.java)
    }
}
