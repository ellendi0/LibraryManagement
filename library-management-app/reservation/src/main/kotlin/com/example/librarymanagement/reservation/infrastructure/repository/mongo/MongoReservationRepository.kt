package com.example.librarymanagement.reservation.infrastructure.repository.mongo

import com.example.librarymanagement.reservation.application.port.out.ReservationRepositoryOutPort
import com.example.librarymanagement.reservation.domain.Reservation
import com.example.librarymanagement.reservation.infrastructure.convertor.ReservationMapper
import com.example.librarymanagement.reservation.infrastructure.entity.MongoReservation
import org.bson.types.ObjectId
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class MongoReservationRepository(
    private val reactiveMongoTemplate: ReactiveMongoTemplate,
    private val reservationMapper: ReservationMapper,
) : ReservationRepositoryOutPort {
    private fun Reservation.toEntity() = reservationMapper.toEntity(this)
    private fun MongoReservation.toDomain() = reservationMapper.toDomain(this)

    override fun save(reservation: Reservation): Mono<Reservation> {
        val result: Mono<MongoReservation> = reactiveMongoTemplate.save(reservation.toEntity())
        return result.map { it.toDomain() }
    }

    override fun findById(reservationId: String): Mono<Reservation> {
        val result: Mono<MongoReservation> =
            reactiveMongoTemplate.findById(ObjectId(reservationId), MongoReservation::class.java)
        return result.map { it.toDomain() }
    }

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
        val result: Flux<MongoReservation> = reactiveMongoTemplate.find(query, MongoReservation::class.java)
        return result.map { it.toDomain() }
    }

    override fun findAllByUserId(userId: String): Flux<Reservation> {
        val result: Flux<MongoReservation> = reactiveMongoTemplate.find(
            Query(Criteria.where(MongoReservation::userId.name).`is`(ObjectId(userId))),
            MongoReservation::class.java
        )
        return result.map { it.toDomain() }
    }

    override fun findFirstByBookIdAndLibraryId(bookId: String, libraryId: String): Mono<Reservation> {
        val criteria = Criteria.where(MongoReservation::bookId.name).`is`(ObjectId(bookId))
            .and(MongoReservation::libraryId.name).`is`(ObjectId(libraryId))

        val query = Query(criteria)
            .with(Sort.by(Sort.Direction.ASC, "_id"))
            .limit(1)

        val result: Mono<MongoReservation> = reactiveMongoTemplate.findOne(query, MongoReservation::class.java)
        return result.map { it.toDomain() }
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
