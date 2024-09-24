package com.example.librarymanagement.repository.mongo

import com.example.librarymanagement.model.domain.User
import com.example.librarymanagement.model.mongo.MongoUser
import com.example.librarymanagement.repository.UserRepository
import com.example.librarymanagement.repository.mongo.mapper.MongoUserMapper
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
class MongoUserRepository(
    private val reactiveMongoTemplate: ReactiveMongoTemplate,
) : UserRepository {
    private fun User.toEntity() = MongoUserMapper.toEntity(this)
    private fun MongoUser.toDomain() = MongoUserMapper.toDomain(this)

    override fun save(user: User): Mono<User> = reactiveMongoTemplate.save(user.toEntity()).map { it.toDomain() }

    override fun findById(userId: String): Mono<User> = reactiveMongoTemplate
            .findById(ObjectId(userId), MongoUser::class.java)
            .map { it.toDomain() }

    override fun findAll(): Flux<User> = reactiveMongoTemplate.findAll(MongoUser::class.java).map { it.toDomain() }

    override fun deleteById(userId: String): Mono<Unit> {
        return reactiveMongoTemplate.findAndRemove(
            Query(Criteria.where("_id").`is`(ObjectId(userId))),
            MongoUser::class.java
        ).then(Mono.just(Unit))
    }

    override fun existsById(userId: String): Mono<Boolean> =
        reactiveMongoTemplate.exists(Query(Criteria.where("_id").`is`(ObjectId(userId))), MongoUser::class.java)

    override fun existsByEmail(email: String): Mono<Boolean> =
        reactiveMongoTemplate.exists(Query(Criteria.where(MongoUser::email.name).`is`(email)), MongoUser::class.java)

    override fun existsByPhoneNumber(phoneNumber: String): Mono<Boolean> {
        return reactiveMongoTemplate.exists(
            Query(Criteria.where(MongoUser::phoneNumber.name).`is`(phoneNumber)),
            MongoUser::class.java
        )
    }

    override fun findByEmailOrPhoneNumber(email: String?, phoneNumber: String?): Mono<User> {
        val query = Query(
            Criteria.where(MongoUser::email.name).`is`(email)
                .orOperator(Criteria(MongoUser::phoneNumber.name).`is`(phoneNumber))
        )
        return reactiveMongoTemplate.findOne(query, MongoUser::class.java).map { it.toDomain() }
    }
}
