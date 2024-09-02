package com.example.librarymanagement.model.mongo

import com.example.librarymanagement.model.mongo.MongoJournal.Companion.COLLECTION_NAME
import jakarta.persistence.Id
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate

@Document(collection = COLLECTION_NAME)
data class MongoJournal(
    @Id val id: ObjectId? = null,
    var dateOfBorrowing: LocalDate,
    var dateOfReturning: LocalDate? = null,
    var userId: ObjectId?,
    val bookPresenceId: ObjectId?
) {
    companion object{
        const val COLLECTION_NAME = "journal"
    }
}
