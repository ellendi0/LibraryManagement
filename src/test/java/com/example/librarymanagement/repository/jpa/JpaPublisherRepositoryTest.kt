package com.example.librarymanagement.repository.jpa

import com.example.librarymanagement.data.PublisherDataFactory
import com.example.librarymanagement.data.JournalDataFactory
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

class JpaPublisherRepositoryTest {
    private val publisherRepositorySpring: PublisherRepositorySpring = mockk()
    private val jpaPublisherRepository = JpaPublisherRepository(publisherRepositorySpring)
    private val jpaPublisher = PublisherDataFactory.createJpaPublisher()

    private val jpaId = JournalDataFactory.JPA_ID
    private val id = jpaId.toString()

    private val publisher = PublisherDataFactory.createPublisher(jpaId)

    @Test
    fun shouldFindAllPublishers() {
        //GIVEN
        val expected = listOf(publisher)

        every { publisherRepositorySpring.findAll() } returns listOf(jpaPublisher)

        //WHEN
        val actual = jpaPublisherRepository.findAll()

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldCreatePublisher() {
        //GIVEN
        val expected = publisher

        every { publisherRepositorySpring.save(any()) } returns jpaPublisher

        //WHEN
        val actual = jpaPublisherRepository.save(expected)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify (exactly = 1) { publisherRepositorySpring.save(any()) }
    }

    @Test
    fun shouldFindById() {
        //GIVEN
        val expected = publisher

        every { publisherRepositorySpring.findById(jpaId) } returns Optional.of(jpaPublisher)

        //WHEN
        val actual = jpaPublisherRepository.findById(id)

        //THEN
        Assertions.assertEquals(expected, actual)
    }
}
//    @Autowired
//    lateinit var jpaPublisherRepository: JpaPublisherRepository
//
//    @Autowired
//    lateinit var entityManager: TestEntityManager
//
//    private val publisher = PublisherDataFactory.createPublisher(PublisherDataFactory.JPA_ID)
//
//    @Test
//    fun shouldSavePublisher() {
//        //given
//        val expected = publisher
//
//        //when
//        val actual = jpaPublisherRepository.save(publisher)
//
//        //then
//        Assertions.assertEquals(expected.name, actual.name)
//        Assertions.assertNotNull(actual.id)
//    }
//
//    @Test
//    fun shouldById() {
//        //given
//        val saved = jpaPublisherRepository.save(publisher)
//        val expectedId = saved.id
//
//        //when
//        val actual = jpaPublisherRepository.findById(expectedId!!)
//
//        //then
//        Assertions.assertNotNull(actual)
//        Assertions.assertEquals(publisher.name, actual?.name)
//    }
//
//    @Test
//    fun shouldFindAllPublishers() {
//        //given
//        val savedPublisher = jpaPublisherRepository.save(publisher)
//
//        //when
//        val allPublishers = jpaPublisherRepository.findAll()
//
//        //then
//        Assertions.assertTrue(allPublishers.isNotEmpty())
//        val foundPublisher = allPublishers.firstOrNull()
//        Assertions.assertNotNull(foundPublisher)
//        Assertions.assertEquals(publisher.name, foundPublisher?.name)
//    }
//}
