package com.example.librarymanagement.dto.mapper.nats

import com.example.internalapi.request.author.create.proto.CreateAuthorRequest
import com.example.internalapi.request.author.update.proto.UpdateAuthorRequest
import com.example.librarymanagement.model.domain.Author
import org.springframework.stereotype.Component
import com.example.internalapi.model.Author as AuthorProto

@Component("natsAuthorMapper")
class AuthorMapper {
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

    fun toAuthorProto(author: Author): AuthorProto {
        return AuthorProto.newBuilder()
            .setId(author.id)
            .setFirstName(author.firstName)
            .setLastName(author.lastName)
            .build()
    }
}
