package com.example.librarymanagement.repository.mongo

import com.example.librarymanagement.model.domain.Library
import com.example.librarymanagement.model.mongo.MongoLibrary
import com.example.librarymanagement.repository.LibraryRepository
import com.example.librarymanagement.repository.mongo.mapper.MongoLibraryMapper
import org.bson.types.ObjectId
import org.springframework.context.annotation.Profile
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
@Profile("mongo")
class MongoLibraryRepository(
    private val mongoTemplate: MongoTemplate
) : LibraryRepository {
    private fun Library.toEntity() = MongoLibraryMapper.toEntity(this)
    private fun MongoLibrary.toDomain() = MongoLibraryMapper.toDomain(this)

    override fun save(library: Library): Library {
        return mongoTemplate.save(library.toEntity()).toDomain()
    }

    override fun findById(libraryId: String): Library? {
        return mongoTemplate.findById(ObjectId(libraryId), MongoLibrary::class.java)?.toDomain()
    }

    override fun findAll(): List<Library> {
        return mongoTemplate.findAll(MongoLibrary::class.java).map { it.toDomain() }
    }

    override fun deleteById(libraryId: String) {
        mongoTemplate.findAndRemove(Query(Criteria
            .where("_id").`is`(ObjectId(libraryId))), MongoLibrary::class.java)
    }
}
