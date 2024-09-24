package com.example.librarymanagement.repository.mongo.mapper

import com.example.librarymanagement.model.domain.Author
import com.example.librarymanagement.model.mongo.MongoAuthor
import org.bson.types.ObjectId

object MongoAuthorMapper {
    fun toEntity(author: Author): MongoAuthor {
        return MongoAuthor(
            id = author.id?.let { ObjectId(it) },
            firstName = author.firstName,
            lastName = author.lastName
        )
    }

    fun toDomain(mongoAuthor: MongoAuthor): Author {
        return Author(
            id = mongoAuthor.id.toString(),
            firstName = mongoAuthor.firstName,
            lastName = mongoAuthor.lastName
        )
    }
}
