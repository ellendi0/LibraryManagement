package com.example.librarymanagement.repository.mongo.mapper

import com.example.librarymanagement.model.domain.BookPresence
import com.example.librarymanagement.model.mongo.MongoBookPresence
import org.bson.types.ObjectId

object MongoBookPresenceMapper {
    fun toEntity(bookPresence: BookPresence): MongoBookPresence {
        return MongoBookPresence(
            id = bookPresence.id?.let { ObjectId(it) },
            availability = bookPresence.availability,
            bookId = ObjectId(bookPresence.bookId),
            libraryId = ObjectId(bookPresence.libraryId),
            userId = bookPresence.userId?.let { ObjectId(it) },
        )
    }

    fun toDomain(mongoBookPresence: MongoBookPresence): BookPresence {
        return BookPresence(
            id = mongoBookPresence.id.toString(),
            availability = mongoBookPresence.availability,
            bookId = mongoBookPresence.bookId.toString(),
            libraryId = mongoBookPresence.libraryId.toString(),
            userId = mongoBookPresence.userId?.toString()
        )
    }
}
