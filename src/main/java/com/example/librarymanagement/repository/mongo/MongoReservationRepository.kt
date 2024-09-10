package com.example.librarymanagement.repository.mongo

import com.example.librarymanagement.model.domain.Reservation
import com.example.librarymanagement.model.mongo.MongoReservation
import com.example.librarymanagement.repository.ReservationRepository
import com.example.librarymanagement.repository.mongo.mapper.MongoReservationMapper
import org.bson.types.ObjectId
import org.springframework.context.annotation.Profile
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
@Profile("mongo")
class MongoReservationRepository(
    private val mongoTemplate: MongoTemplate
) : ReservationRepository {
    private fun Reservation.toEntity() = MongoReservationMapper.toEntity(this)
    private fun MongoReservation.toDomain() = MongoReservationMapper.toDomain(this)

    override fun save(reservation: Reservation): Reservation {
        return mongoTemplate.save(reservation.toEntity()).toDomain()
    }

    override fun findById(reservationId: String): Reservation? {
        return mongoTemplate.findById(ObjectId(reservationId), MongoReservation::class.java)?.toDomain()
    }

    override fun deleteById(reservationId: String) {
        val query = Query(Criteria.where("_id").`is`(ObjectId(reservationId)))
        mongoTemplate.findAndRemove(query, MongoReservation::class.java)
    }

    override fun findAllByBookIdAndUserId(bookId: String, userId: String): List<Reservation> {
        val query = Query(Criteria
            .where(MongoReservation::bookId.name).`is`(ObjectId(bookId))
            .and(MongoReservation::userId.name).`is`(ObjectId(userId)))
        return mongoTemplate.find(query, MongoReservation::class.java).map { it.toDomain() }
    }

    override fun findAllByLibraryId(libraryId: String): List<Reservation> {
        return mongoTemplate.find(Query(Criteria
            .where(MongoReservation::libraryId.name).`is`(ObjectId(libraryId))), MongoReservation::class.java)
            .map { it.toDomain() }
    }

    override fun findAllByUserId(userId: String): List<Reservation> {
        return mongoTemplate.find(Query(Criteria
            .where(MongoReservation::userId.name).`is`(ObjectId(userId))), MongoReservation::class.java)
            .map { it.toDomain() }
    }

    override fun findAllByBookId(bookId: String): List<Reservation> {
        return mongoTemplate.find(Query(Criteria
            .where(MongoReservation::bookId.name).`is`(ObjectId(bookId))), MongoReservation::class.java)
            .map { it.toDomain() }
    }

    override fun findFirstByBookIdAndLibraryIdOrLibraryIsNull(bookId: String, libraryId: String?): Reservation? {
        val criteria = Criteria.where(MongoReservation::bookId.name).`is`(ObjectId(bookId))
        when {
            libraryId != null -> criteria.and(MongoReservation::libraryId.name).`is`(ObjectId(libraryId))
            else -> criteria.orOperator(Criteria.where(MongoReservation::libraryId.name).exists(false))
        }

        val query = Query(criteria)
            .with(Sort.by(Sort.Direction.ASC, "_id"))
            .limit(1)

        return mongoTemplate.findOne(query, MongoReservation::class.java)?.toDomain()
    }

    override fun existsByBookIdAndUserId(bookId: String, userId: String): Boolean {
        val query = Query(Criteria
            .where(MongoReservation::bookId.name).`is`(ObjectId(bookId))
            .and(MongoReservation::userId.name).`is`(ObjectId(userId))
        )
        return mongoTemplate.exists(query, MongoReservation::class.java)
    }

    override fun findAllByBookIdAndLibraryId(bookId: String, libraryId: String): List<Reservation> {
        val query = Query(Criteria
            .where(MongoReservation::bookId.name).`is`(ObjectId(bookId))
            .and(MongoReservation::libraryId.name).`is`(ObjectId(libraryId))
        )
        return mongoTemplate.find(query, MongoReservation::class.java).map { it.toDomain() }
    }
}
