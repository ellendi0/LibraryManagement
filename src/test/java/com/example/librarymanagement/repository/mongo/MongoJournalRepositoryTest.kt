package com.example.librarymanagement.repository.mongo

import com.example.librarymanagement.data.JournalDataFactory
import com.example.librarymanagement.model.mongo.MongoJournal
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.data.mongodb.core.MongoTemplate

class MongoJournalRepositoryTest {
    private val mongoTemplate: MongoTemplate = mockk()
    private val mongoJournalRepository = MongoJournalRepository(mongoTemplate)

    private val mongoId = JournalDataFactory.MONGO_ID
    private val id = mongoId.toString()

    private val journal = JournalDataFactory.createJournal(id)
    private val mongoJournal = JournalDataFactory.createMongoJournal(mongoId)

    @Test
    fun save() {
        //GIVEN
        val expected = journal

        every { mongoTemplate.save(mongoJournal) } returns mongoJournal

        //WHEN
        val actual = mongoJournalRepository.save(journal)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify (exactly = 1) {mongoTemplate.save(mongoJournal)}
    }

    @Test
    fun findById() {
        //GIVEN
        val expected = journal

        every { mongoTemplate.findById(mongoId, MongoJournal::class.java) } returns mongoJournal

        //WHEN
        val actual = mongoJournalRepository.findById(mongoId.toString())

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun deleteById() {
        //GIVEN
        every { mongoTemplate.findAndRemove(any(), MongoJournal::class.java) } returns mongoJournal

        //WHEN
        mongoJournalRepository.deleteById(mongoId.toString())

        //THEN
        verify(exactly = 1) { mongoTemplate.findAndRemove(any(), MongoJournal::class.java) }
    }

    @Test
    fun findAllByUserId() {
        //GIVEN
        val expected = listOf(journal)

        every { mongoTemplate.find(any(), MongoJournal::class.java) } returns listOf(mongoJournal)

        //WHEN
        val actual = mongoJournalRepository.findAllByUserId(id)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify (exactly = 1) {
            mongoTemplate
                .find(match { it.queryObject["userId"] == mongoId },
                    MongoJournal::class.java)
        }
    }

    @Test
    fun findByBookPresenceIdAndUserIdAndDateOfReturningIsNull() {
        //GIVEN
        val expected = journal

        every { mongoTemplate.find(any(), MongoJournal::class.java) }
            .returns(listOf(mongoJournal))

        //WHEN
        val actual = mongoJournalRepository.findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(id, id)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) {
            mongoTemplate.find(
                match {
                    it.queryObject[MongoJournal::bookPresenceId.name] == mongoId &&
                    it.queryObject[MongoJournal::userId.name] == mongoId &&
                    it.queryObject[MongoJournal::dateOfReturning.name] == null
                },
                MongoJournal::class.java
            )
        }
    }
}
