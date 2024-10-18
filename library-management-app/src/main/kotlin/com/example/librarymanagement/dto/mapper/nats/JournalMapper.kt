package com.example.librarymanagement.dto.mapper.nats

import com.example.librarymanagement.model.domain.Journal
import org.springframework.stereotype.Component
import com.example.internalapi.model.Journal as JournalProto

@Component("natsJournalMapper")
class JournalMapper {
    fun toJournalProto(journal: Journal): JournalProto {
        return JournalProto.newBuilder()
            .setId(journal.id)
            .setDateOfBorrowing(journal.dateOfBorrowing.toString())
            .setDateOfReturning(journal.dateOfReturning.toString())
            .setUserId(journal.userId)
            .setBookPresenceId(journal.bookPresenceId)
            .build()
    }
}
