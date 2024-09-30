package com.example.librarymanagement.dto.mapper.nats

import com.example.internalapi.model.Availability
import com.example.librarymanagement.model.domain.BookPresence
import org.springframework.stereotype.Component
import com.example.internalapi.model.BookPresence as BookPresenceProto

@Component("natsBookPresenceMapper")
class BookPresenceMapper {
    fun toBookPresenceProto(bookPresence: BookPresence): BookPresenceProto {
        return BookPresenceProto.newBuilder()
            .setId(bookPresence.id)
            .setAvailability(Availability.valueOf(bookPresence.availability.toString()))
            .setBookId(bookPresence.bookId)
            .setLibraryId(bookPresence.libraryId)
            .setUserId(bookPresence.userId)
            .build()
    }
}
