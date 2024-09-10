package com.example.librarymanagement.repository.mongo

import com.example.librarymanagement.data.BookPresenceDataFactory
import com.example.librarymanagement.data.JournalDataFactory
import com.example.librarymanagement.data.UserDataFactory
import com.example.librarymanagement.model.enums.Availability
import com.example.librarymanagement.model.mongo.MongoBookPresence
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.data.mongodb.core.MongoTemplate

class MongoBookPresenceRepositoryTest {
    private val mongoTemplate: MongoTemplate = mockk()
    private val journalRepository: MongoJournalRepository = mockk()
    private val mongoBookPresenceRepository = MongoBookPresenceRepository(mongoTemplate, journalRepository)

    private val mongoId = BookPresenceDataFactory.MONGO_ID
    private val id = mongoId.toString()

    private val bookPresence = BookPresenceDataFactory.createBookPresence(id)
    private val mongoBookPresence = BookPresenceDataFactory.createMongoBookPresence(mongoId)

    @Test
    fun shouldSaveBookPresence() {
        //GIVEN
        val expected = bookPresence

        every { mongoTemplate.save(mongoBookPresence) } returns mongoBookPresence

        //WHEN
        val actual = mongoBookPresenceRepository.save(bookPresence)

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldAddBookToUser() {
        // GIVEN
        val mongoJournal = JournalDataFactory.createJournal(id)
        val unavailableMongoBookPresence =
            mongoBookPresence.copy(availability = Availability.UNAVAILABLE, userId = mongoId)
        val expected = bookPresence.copy(availability = Availability.UNAVAILABLE, userId = id)
        val user = UserDataFactory.createUser(id)

        every { mongoTemplate.find(any(), MongoBookPresence::class.java) }
            .returns(listOf(mongoBookPresence))

        every { journalRepository.save(any()) } returns mongoJournal
        every { mongoTemplate.save(unavailableMongoBookPresence) } returns unavailableMongoBookPresence

        // WHEN
        val actual = mongoBookPresenceRepository.addBookToUser(user, id, id)

        // THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { journalRepository.save(any()) }
        verify(exactly = 1) { mongoTemplate.save(unavailableMongoBookPresence) }
    }

    @Test
    fun shouldRemoveBookFromUser() {
        // GIVEN
        val mongoJournal = JournalDataFactory.createJournal(id)
        val unavailableMongoBookPresence = mongoBookPresence
        val availableMongoBookPresence = mongoBookPresence.copy(availability = Availability.AVAILABLE, userId = null)
        val expected = bookPresence.copy(availability = Availability.AVAILABLE, userId = null)
        val user = UserDataFactory.createUser(id)

        every { mongoTemplate.find(any(), MongoBookPresence::class.java) }
            .returns(listOf(unavailableMongoBookPresence))

        every { journalRepository.findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(any(), any()) }
            .returns(mongoJournal)
        every { journalRepository.save(any()) } returns mongoJournal
        every { mongoTemplate.save(availableMongoBookPresence) } returns availableMongoBookPresence

        // WHEN
        val actual = mongoBookPresenceRepository.removeBookFromUser(user, id, id)

        // THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { journalRepository.save(any()) }
        verify(exactly = 1) { mongoTemplate.save(availableMongoBookPresence) }
    }

    @Test
    fun shouldFindById() {
        //GIVEN
        val expected = bookPresence

        every { mongoTemplate.findById(mongoId, MongoBookPresence::class.java) } returns mongoBookPresence

        //WHEN
        val actual = mongoBookPresenceRepository.findById(id)

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldFindAllByBookId() {
        //GIVEN
        val expected = listOf(bookPresence)

        every { mongoTemplate.find(any(), MongoBookPresence::class.java) } returns listOf(mongoBookPresence)

        // WHEN
        val actual = mongoBookPresenceRepository.findAllByBookId(id)

        // THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) {
            mongoTemplate
                .find(match { query -> query.queryObject["bookId"] == mongoId }, MongoBookPresence::class.java)
        }
    }

    @Test
    fun shouldFindAllByLibraryId() {
        //GIVEN
        val expected = listOf(bookPresence)

        every { mongoTemplate.find(any(), MongoBookPresence::class.java) } returns listOf(mongoBookPresence)

        // WHEN
        val actual = mongoBookPresenceRepository.findAllByLibraryId(id)

        // THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) {
            mongoTemplate
                .find(match { query -> query.queryObject["libraryId"] == mongoId }, MongoBookPresence::class.java)
        }
    }

    @Test
    fun shouldFindAllByUserId() {
        //GIVEN
        val expected = listOf(bookPresence)

        every { mongoTemplate.find(any(), MongoBookPresence::class.java) } returns listOf(mongoBookPresence)

        // WHEN
        val actual = mongoBookPresenceRepository.findAllByUserId(id)

        // THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) {
            mongoTemplate
                .find(match { query -> query.queryObject["userId"] == mongoId }, MongoBookPresence::class.java)
        }
    }

    @Test
    fun shouldFindAllByLibraryIdAndBookId() {
        //GIVEN
        val expected = listOf(bookPresence)

        every { mongoTemplate.find(any(), MongoBookPresence::class.java) } returns listOf(mongoBookPresence)

        // WHEN
        val actual = mongoBookPresenceRepository.findAllByLibraryIdAndBookId(id, id)

        // THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) {
            mongoTemplate.find(
                match {
                    it.queryObject["libraryId"] == mongoId && it.queryObject["bookId"] == mongoId
                },
                MongoBookPresence::class.java
            )
        }
    }

    @Test
    fun shouldFindAllByLibraryIdAndBookIdAndAvailability() {
        //GIVEN
        val expected = listOf(bookPresence)

        every { mongoTemplate.find(any(), MongoBookPresence::class.java) } returns listOf(mongoBookPresence)

        // WHEN
        val actual =
            mongoBookPresenceRepository.findAllByLibraryIdAndBookIdAndAvailability(id, id, Availability.AVAILABLE)

        // THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) {
            mongoTemplate.find(
                match {
                    it.queryObject["libraryId"] == mongoId &&
                    it.queryObject["bookId"] == mongoId &&
                    it.queryObject["availability"] == Availability.AVAILABLE
                },
                MongoBookPresence::class.java
            )
        }
    }

    @Test
    fun shouldDeleteById() {
        //GIVEN
        every { mongoTemplate.findAndRemove(any(), MongoBookPresence::class.java) } returns mongoBookPresence

        //WHEN
        mongoBookPresenceRepository.deleteById(id)

        //THEN
        verify(exactly = 1) { mongoTemplate.findAndRemove(any(), MongoBookPresence::class.java) }
    }
}
