package com.example.librarymanagement.service.impl

import com.example.librarymanagement.exception.EntityNotFoundException
import com.example.librarymanagement.model.entity.Publisher
import com.example.librarymanagement.repository.PublisherRepository
import com.example.librarymanagement.service.PublisherService
import org.springframework.stereotype.Service

@Service
class PublisherServiceImpl(private val publisherRepository: PublisherRepository): PublisherService {

    override fun createPublisher(publisher: Publisher): Publisher = publisherRepository.save(publisher)

    override fun updatePublisher(id: Long, updatedPublisher: Publisher): Publisher {
        val publisher = getPublisherById(id).apply { this.name = updatedPublisher.name }
        return publisherRepository.save(publisher)
    }

    override fun getPublisherById(id: Long): Publisher {
        return publisherRepository.findById(id).orElseThrow { throw EntityNotFoundException("Publisher") }
    }

    override fun getAllPublishers(): List<Publisher> = publisherRepository.findAll()
}