package com.example.librarymanagement.service.impl

import com.example.librarymanagement.data.PublisherDataFactory
import com.example.librarymanagement.repository.PublisherRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PublisherServiceImplTest {
    private val publisherRepository: PublisherRepository = mockk()
    private val publisherService = PublisherServiceImpl(publisherRepository)

    private val id = "1"

    private val publisher = PublisherDataFactory.createPublisher(id)

    @Test
    fun shouldFindAll() {
        //GIVEN
        val expected = listOf(publisher)
        every { publisherRepository.findAll() } returns (listOf(publisher))

        //WHEN
        val actual = publisherService.getAllPublishers()

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { publisherRepository.findAll() }
    }

    @Test
    fun shouldFindById() {
        //GIVEN
        val expected = publisher

        every { publisherRepository.findById(id) } returns publisher

        //WHEN
        val actual = publisherService.getPublisherById(id)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { publisherRepository.findById(id) }
    }

    @Test
    fun shouldCreatePublisher() {
        //GIVEN
        val expected = publisher
        every { publisherRepository.save(publisher) } returns publisher

        //WHEN
        val actual = publisherService.createPublisher(publisher)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { publisherRepository.save(publisher) }
    }

    @Test
    fun shouldUpdatePublisher() {
        //GIVEN
        val excepted = publisher.copy(name = "Updated")
        every { publisherRepository.findById(id) } returns publisher
        every { publisherRepository.save(excepted) } returns excepted

        //WHEN
        val actual = publisherService.updatePublisher(excepted)

        //THEN
        Assertions.assertEquals(excepted, actual)
        verify(exactly = 1) { publisherRepository.findById(id) }
        verify(exactly = 1) { publisherRepository.save(excepted) }
    }
}
