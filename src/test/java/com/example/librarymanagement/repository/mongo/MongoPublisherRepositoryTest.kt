package com.example.librarymanagement.repository.mongo

import com.example.librarymanagement.data.PublisherDataFactory
import com.example.librarymanagement.model.mongo.MongoPublisher
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.data.mongodb.core.MongoTemplate

//@DataMongoTest
//@ActiveProfiles(profiles = ["test", "mongo"])
//@Import(MongoPublisherRepository::class)
class MongoPublisherRepositoryTest {
    private val mongoTemplate: MongoTemplate = mockk()
    private val mongoPublisherRepository = MongoPublisherRepository(mongoTemplate)

    private val mongoId = PublisherDataFactory.MONGO_ID
    private val id = mongoId.toString()

    private val publisher = PublisherDataFactory.createPublisher(id)
    private val mongoPublisher = PublisherDataFactory.createMongoPublisher(mongoId)

    @Test
    fun shouldFindAllPublishers() {
        //GIVEN
        val expected = listOf(publisher)

        every { mongoTemplate.findAll(MongoPublisher::class.java) } returns listOf(mongoPublisher)

        //WHEN
        val actual = mongoPublisherRepository.findAll()

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldCreatePublisher() {
        //GIVEN
        val expected = publisher

        every { mongoTemplate.save(mongoPublisher) } returns mongoPublisher

        //WHEN
        val actual = mongoPublisherRepository.save(expected)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { mongoTemplate.save(mongoPublisher) }
    }

    @Test
    fun shouldFindById() {
        //GIVEN
        val expected = publisher

        every { mongoTemplate.findById(mongoId, MongoPublisher::class.java) } returns mongoPublisher

        //WHEN
        val actual = mongoPublisherRepository.findById(id)

        //THEN
        Assertions.assertEquals(expected, actual)
    }
}

