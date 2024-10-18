package com.example.librarymanagement.bookpresence.infrastructure.convertor

import com.example.librarymanagement.bookpresence.domain.BookPresence
import com.example.librarymanagement.bookpresence.infrastructure.entity.MongoBookPresence
import org.bson.types.ObjectId
import org.springframework.stereotype.Component
import com.example.internalapi.model.Availability as AvailabilityProto
import com.example.internalapi.model.BookPresence as BookPresenceProto

@Component
class BookPresenceMapper {
    fun toBookPresenceProto(bookPresence: BookPresence): BookPresenceProto {
        return BookPresenceProto.newBuilder()
            .setId(bookPresence.id)
            .setAvailability(AvailabilityProto.valueOf(bookPresence.availability.toString()))
            .setBookId(bookPresence.bookId)
            .setLibraryId(bookPresence.libraryId)
            .setUserId(bookPresence.userId ?: " ")
            .build()
    }

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
