package com.example.librarymanagement.repository.jpa

import com.example.librarymanagement.model.domain.Publisher
import com.example.librarymanagement.model.jpa.JpaPublisher
import com.example.librarymanagement.repository.PublisherRepository
import com.example.librarymanagement.repository.jpa.mapper.JpaPublisherMapper
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class JpaPublisherRepository(
        private val publisherRepository: PublisherRepositorySpring
) : PublisherRepository {
    fun Publisher.toEntity() = JpaPublisherMapper.toEntity(this)
    fun JpaPublisher.toDomain() = JpaPublisherMapper.toDomain(this)

    override fun save(publisher: Publisher): Publisher {
        return publisherRepository.save(publisher.toEntity()).toDomain()
    }

    override fun findById(publisherId: String): Publisher? {
        return publisherRepository.findByIdOrNull(publisherId.toLong())?.toDomain()
    }

    override fun findAll(): List<Publisher> {
        return publisherRepository.findAll().map { it.toDomain() }
    }
}

@Repository
interface PublisherRepositorySpring: JpaRepository<JpaPublisher, Long>
