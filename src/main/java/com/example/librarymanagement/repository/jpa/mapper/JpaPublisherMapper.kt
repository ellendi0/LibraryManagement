package com.example.librarymanagement.repository.jpa.mapper

import com.example.librarymanagement.model.domain.Publisher
import com.example.librarymanagement.model.jpa.JpaPublisher

object JpaPublisherMapper {
    fun toEntity(publisher: Publisher): JpaPublisher{
        return JpaPublisher(
                id = publisher.id?.toLong(),
                name = publisher.name
        )
    }

    fun toDomain(jpaPublisher: JpaPublisher): Publisher{
        return Publisher(
                id = jpaPublisher.id?.toString(),
                name = jpaPublisher.name
        )
    }
}
