package com.example.librarymanagement.repository.mongo

import com.example.librarymanagement.model.domain.Reservation
import com.example.librarymanagement.model.mongo.MongoReservation
import com.example.librarymanagement.repository.ReservationRepository
import com.example.librarymanagement.repository.mongo.mapper.MongoReservationMapper
import org.bson.types.ObjectId
import org.springframework.context.annotation.Profile
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
@Profile("mongo")
class MongoReservationRepository(
    private val reactiveMongoTemplate: ReactiveMongoTemplate,
) : ReservationRepository {
    private fun Reservation.toEntity() = MongoReservationMapper.toEntity(this)
    private fun MongoReservation.toDomain() = MongoReservationMapper.toDomain(this)

    override fun save(reservation: Reservation): Mono<Reservation> =
        reactiveMongoTemplate.save(reservation.toEntity()).map { it.toDomain() }

    override fun findById(reservationId: String): Mono<Reservation> =
        reactiveMongoTemplate.findById(ObjectId(reservationId), MongoReservation::class.java).map { it.toDomain() }

    override fun deleteById(reservationId: String): Mono<Unit> {
        val query = Query(Criteria.where("_id").`is`(ObjectId(reservationId)))
        return reactiveMongoTemplate.findAndRemove(query, MongoReservation::class.java).then(Mono.just(Unit))
    }

    override fun findAllByBookIdAndUserId(bookId: String, userId: String): Flux<Reservation> {
        val query = Query(
            Criteria
                .where(MongoReservation::bookId.name).`is`(ObjectId(bookId))
                .and(MongoReservation::userId.name).`is`(ObjectId(userId))
        )
        return reactiveMongoTemplate.find(query, MongoReservation::class.java).map { it.toDomain() }
    }

    override fun findAllByUserId(userId: String): Flux<Reservation> {
        return reactiveMongoTemplate.find(
            Query(Criteria.where(MongoReservation::userId.name).`is`(ObjectId(userId))),
            MongoReservation::class.java
        )
            .map { it.toDomain() }
    }

    override fun findFirstByBookIdAndLibraryId(bookId: String, libraryId: String): Mono<Reservation> {
        val criteria = Criteria.where(MongoReservation::bookId.name).`is`(ObjectId(bookId))
            .and(MongoReservation::libraryId.name).`is`(ObjectId(libraryId))

        val query = Query(criteria)
            .with(Sort.by(Sort.Direction.ASC, "_id"))
            .limit(1)

        return reactiveMongoTemplate.findOne(query, MongoReservation::class.java).map { it.toDomain() }
    }

    override fun existsByBookIdAndUserId(bookId: String, userId: String): Mono<Boolean> {
        val query = Query(
            Criteria
                .where(MongoReservation::bookId.name).`is`(ObjectId(bookId))
                .and(MongoReservation::userId.name).`is`(ObjectId(userId))
        )
        return reactiveMongoTemplate.exists(query, MongoReservation::class.java)
    }
}
