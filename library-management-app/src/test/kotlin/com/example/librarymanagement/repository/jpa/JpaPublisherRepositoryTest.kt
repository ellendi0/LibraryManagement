package com.example.librarymanagement.repository.jpa

import com.example.librarymanagement.data.PublisherDataFactory
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import reactor.test.StepVerifier
import java.util.*

class JpaPublisherRepositoryTest {
    private val publisherRepositorySpring: PublisherRepositorySpring = mockk()
    private val jpaPublisherRepository = JpaPublisherRepository(publisherRepositorySpring)
    private val jpaPublisher = PublisherDataFactory.createJpaPublisher()

    private val publisher = PublisherDataFactory.createPublisher()

    @Test
    fun `should find all publishers`() {
        //GIVEN
        val expected = publisher

        every { publisherRepositorySpring.findAll() } returns listOf(jpaPublisher)

        //WHEN
        val result = StepVerifier.create(jpaPublisherRepository.findAll())

        //THEN
        result
            .assertNext { actual -> Assertions.assertEquals(expected, actual) }
            .verifyComplete()
    }

    @Test
    fun `should create publisher`() {
        //GIVEN
        val expected = publisher

        every { publisherRepositorySpring.save(any()) } returns jpaPublisher

        //WHEN
        val result = StepVerifier.create(jpaPublisherRepository.save(expected))

        //THEN
        result
            .assertNext { actual ->
                Assertions.assertEquals(expected, actual)
                verify(exactly = 1) { publisherRepositorySpring.save(any()) }
            }
            .verifyComplete()
    }

    @Test
    fun `should find by id`() {
        //GIVEN
        val expected = publisher

        every { publisherRepositorySpring.findById(ID) } returns Optional.of(jpaPublisher)

        //WHEN
        val result = StepVerifier.create(jpaPublisherRepository.findById(ID.toString()))

        //THEN
        result
            .assertNext { actual ->
                Assertions.assertEquals(expected, actual)
                verify(exactly = 1) { publisherRepositorySpring.findById(ID) }
            }
            .verifyComplete()
    }

    companion object {
        const val ID = PublisherDataFactory.JPA_ID
    }
}
