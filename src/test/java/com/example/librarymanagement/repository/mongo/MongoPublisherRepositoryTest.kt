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

    private val publisher = PublisherDataFactory.createPublisher(mongoId)
    private val mongoPublisher = PublisherDataFactory.createMongoPublisher()

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
    
//    @Autowired
//    lateinit var mongoPublisherRepository: MongoPublisherRepository
//
//    private val publisher = PublisherDataFactory.createPublisher().copy(id = null)
//
//    @Test
//    fun shouldSavePublisher() {
//        //given
//        val expected = publisher
//
//        //when
//        val actual = mongoPublisherRepository.save(publisher)
//
//        //then
//        Assertions.assertEquals(expected.name, actual.name)
//        Assertions.assertNotNull(actual.id)
//    }
//
//    @Test
//    fun shouldById() {
//        //given
//        val saved = mongoPublisherRepository.save(publisher)
//        val expectedId = saved.id
//
//        //when
//        val actual = mongoPublisherRepository.findById(expectedId!!)
//
//        //then
//        Assertions.assertNotNull(actual)
//        Assertions.assertEquals(publisher.name, actual?.name)
//    }
//
//    @Test
//    fun shouldFindAllPublishers() {
//        //given
//        val savedPublisher = mongoPublisherRepository.save(publisher)
//
//        //when
//        val allPublishers = mongoPublisherRepository.findAll()
//
//        //then
//        Assertions.assertTrue(allPublishers.isNotEmpty())
//        val foundPublisher = allPublishers.firstOrNull()
//        Assertions.assertNotNull(foundPublisher)
//        Assertions.assertEquals(publisher.name, foundPublisher?.name)
//    }
