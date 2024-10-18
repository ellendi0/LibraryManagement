package com.example.librarymanagement.user.infrastructure.repository.mongo

import com.example.librarymanagement.user.application.port.out.UserRepositoryOutPort
import com.example.librarymanagement.user.domain.User
import com.example.librarymanagement.user.infrastructure.convertor.UserMapper
import com.example.librarymanagement.user.infrastructure.entity.MongoUser
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class MongoUserRepository(
    private val reactiveMongoTemplate: ReactiveMongoTemplate,
    private val userMapper: UserMapper
) : UserRepositoryOutPort {
    private fun User.toEntity() = userMapper.toEntity(this)
    private fun MongoUser.toDomain() = userMapper.toDomain(this)

    override fun save(user: User): Mono<User> {
        val result: Mono<MongoUser> = reactiveMongoTemplate.save(user.toEntity())
        return result.map { it.toDomain() }
    }

    override fun findById(userId: String): Mono<User> {
        val result: Mono<MongoUser> = reactiveMongoTemplate.findById(ObjectId(userId), MongoUser::class.java)
        return result.map { it.toDomain() }
    }

    override fun findAll(): Flux<User> {
        val result: Flux<MongoUser> = reactiveMongoTemplate.findAll(MongoUser::class.java)
        return result.map { it.toDomain() }
    }

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
}
