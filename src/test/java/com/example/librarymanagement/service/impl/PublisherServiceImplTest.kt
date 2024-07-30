package com.example.librarymanagement.service.impl

import com.example.librarymanagement.data.TestDataFactory
import com.example.librarymanagement.repository.PublisherRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

class PublisherServiceImplTest {
    private val publisherRepository: PublisherRepository = mockk()
    private val publisherService = PublisherServiceImpl(publisherRepository)

    private val publisher = TestDataFactory.createPublisher()

    @Test
    fun shouldFindAll() {
        every { publisherRepository.findAll() } returns (listOf(publisher))

        Assertions.assertEquals(listOf(publisher), publisherService.getAllPublishers())
        verify(exactly = 1) { publisherRepository.findAll() }
    }

    @Test
    fun shouldFindById() {
        every { publisherRepository.findById(1) } returns Optional.of(publisher)

        Assertions.assertEquals(publisher, publisherService.getPublisherById(1))
        verify(exactly = 1) { publisherRepository.findById(1) }
    }

    @Test
    fun shouldCreatePublisher() {
        every { publisherRepository.save(publisher) } returns publisher

        Assertions.assertEquals(publisher, publisherService.createPublisher(publisher))
        verify(exactly = 1) { publisherRepository.save(publisher) }
    }

    @Test
    fun shouldUpdatePublisher() {
        val updatedPublisher = publisher.copy(name = "Updated")
        every { publisherRepository.findById(1) } returns Optional.of(publisher)
        every { publisherRepository.save(updatedPublisher) } returns updatedPublisher

        Assertions.assertEquals(updatedPublisher, publisherService.updatePublisher(1, updatedPublisher))
        verify(exactly = 1) { publisherRepository.findById(1) }
        verify(exactly = 1) { publisherRepository.save(updatedPublisher) }
    }
}