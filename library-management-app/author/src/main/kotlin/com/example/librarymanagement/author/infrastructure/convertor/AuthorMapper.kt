package com.example.librarymanagement.author.infrastructure.convertor

import com.example.internalapi.request.author.create.proto.CreateAuthorRequest
import com.example.internalapi.request.author.update.proto.UpdateAuthorRequest
import com.example.librarymanagement.author.domain.Author
import com.example.librarymanagement.author.infrastructure.entity.MongoAuthor
import org.bson.types.ObjectId
import org.springframework.stereotype.Component

@Component
class AuthorMapper {
    fun toEntity(author: Author): MongoAuthor {
        return MongoAuthor(
            id = author.id?.let { ObjectId(it) },
            firstName = author.firstName,
            lastName = author.lastName
        )
    }

    fun toDomain(mongoAuthor: MongoAuthor): Author {
        return Author(
            id = mongoAuthor.id.toString(),
            firstName = mongoAuthor.firstName,
            lastName = mongoAuthor.lastName
        )
    }

    fun toAuthor(request: CreateAuthorRequest): Author {
        return Author(
            id = request.author.id.ifEmpty { null },
            firstName = request.author.firstName,
            lastName = request.author.lastName
        )
    }

    fun toAuthor(request: UpdateAuthorRequest): Author {
        return Author(
            id = request.author.id,
            firstName = request.author.firstName,
            lastName = request.author.lastName
        )
    }

    fun toAuthorProto(author: Author): com.example.internalapi.model.Author {
        return com.example.internalapi.model.Author.newBuilder()
            .setId(author.id)
            .setFirstName(author.firstName)
            .setLastName(author.lastName)
            .build()
    }
}
