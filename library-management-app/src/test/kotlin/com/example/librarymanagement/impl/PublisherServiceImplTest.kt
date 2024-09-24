package com.example.librarymanagement.service.impl

import com.example.librarymanagement.data.PublisherDataFactory
import com.example.librarymanagement.repository.PublisherRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class PublisherServiceImplTest {
    private val id = PublisherDataFactory.ID

    private val publisherRepository: PublisherRepository = mockk()
    private val publisherService = PublisherServiceImpl(publisherRepository)
    private val publisher = PublisherDataFactory.createPublisher(id)

    @Test
    fun `should find all publisher`() {
        // GIVEN
        val expected = publisher

        every { publisherRepository.findAll() } returns Flux.just(publisher)

        // WHEN
        val result = StepVerifier.create(publisherService.getAllPublishers())

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
            verify(exactly = 1) { publisherRepository.findAll() }
        }.verifyComplete()
    }


    @Test
    fun `should find by id `() {
        // GIVEN
        val expected = publisher

        every { publisherRepository.findById(any()) } returns Mono.just(publisher)

        // WHEN
        val result = StepVerifier.create(publisherService.getPublisherById(id))

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
            verify(exactly = 1) { publisherRepository.findById(any()) }
        }.verifyComplete()
    }

    @Test
    fun `should create publisher`() {
        // GIVEN
        val expected = publisher

        every { publisherRepository.save(publisher) } returns Mono.just(publisher)

        // WHEN
        val result = StepVerifier.create(publisherService.createPublisher(publisher))

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
            verify(exactly = 1) { publisherRepository.save(publisher) }
        }.verifyComplete()
    }

    @Test
    fun `should update publisher`() {
        // GIVEN
        val expected = publisher

        every { publisherRepository.findById(id) } returns Mono.just(publisher)
        every { publisherRepository.save(expected) } returns Mono.just(expected)

        // WHEN
        val result = StepVerifier.create(publisherService.updatePublisher(expected))

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
            verify(exactly = 1) { publisherRepository.findById(id) }
            verify(exactly = 1) { publisherRepository.save(expected) }
        }.verifyComplete()
    }
}
