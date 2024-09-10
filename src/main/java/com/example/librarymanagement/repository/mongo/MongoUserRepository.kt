package com.example.librarymanagement.repository.mongo

import com.example.librarymanagement.model.domain.User
import com.example.librarymanagement.model.mongo.MongoUser
import com.example.librarymanagement.repository.UserRepository
import com.example.librarymanagement.repository.mongo.mapper.MongoUserMapper
import org.bson.types.ObjectId
import org.springframework.context.annotation.Profile
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
@Profile("mongo")
class MongoUserRepository(
    private val mongoTemplate: MongoTemplate
) : UserRepository {
    private fun User.toEntity() = MongoUserMapper.toEntity(this)
    private fun MongoUser.toDomain() = MongoUserMapper.toDomain(this)

    override fun save(user: User): User {
        return mongoTemplate.save(user.toEntity()).toDomain()
    }

    override fun findById(userId: String): User? {
        return mongoTemplate.findById(ObjectId(userId), MongoUser::class.java)?.toDomain()
    }

    override fun findAll(): List<User> {
        return mongoTemplate.findAll(MongoUser::class.java).map { it.toDomain() }
    }

    override fun deleteById(userId: String) {
        mongoTemplate.findAndRemove(Query(Criteria.where("_id").`is`(ObjectId(userId))), MongoUser::class.java)
    }

    override fun existsByEmail(email: String): Boolean {
        return mongoTemplate.exists(Query(Criteria.where("email").`is`(email)), MongoUser::class.java)
    }

    override fun existsByPhoneNumber(phoneNumber: String): Boolean {
        return mongoTemplate.exists(Query(Criteria
            .where("phoneNumber").`is`(phoneNumber)), MongoUser::class.java)
    }

    override fun findByEmailOrPhoneNumber(email: String?, phoneNumber: String?): User? {
        val query = Query(Criteria.where("email").`is`(email)
            .orOperator(Criteria("phoneNumber").`is`(phoneNumber)))
        return mongoTemplate.findOne(query, MongoUser::class.java)?.toDomain()
    }
}
