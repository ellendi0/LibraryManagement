package com.example.librarymanagement.service.impl

import com.example.librarymanagement.exception.EntityNotFoundException
import com.example.librarymanagement.model.domain.Publisher
import com.example.librarymanagement.repository.PublisherRepository
import com.example.librarymanagement.service.PublisherService
import org.springframework.stereotype.Service

@Service
class PublisherServiceImpl(private val publisherRepository: PublisherRepository): PublisherService {

    override fun createPublisher(publisher: Publisher): Publisher = publisherRepository.save(publisher)

    override fun updatePublisher(updatedPublisher: Publisher): Publisher {
        val publisher = getPublisherById(updatedPublisher.id!!).copy(name = updatedPublisher.name)
        return publisherRepository.save(publisher)
    }

    override fun getPublisherById(id: String): Publisher {
        return publisherRepository.findById(id) ?: throw EntityNotFoundException("Publisher")
    }

    override fun getAllPublishers(): List<Publisher> = publisherRepository.findAll()
}
