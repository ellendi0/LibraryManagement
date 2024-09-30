package com.example.com.example.gateway.validation

import com.example.com.example.gateway.validation.model.AuthorDto
import com.example.internalapi.model.Author
import com.example.internalapi.request.author.create.proto.CreateAuthorRequest
import com.example.internalapi.request.author.update.proto.UpdateAuthorRequest
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class AuthorRequestValidation(private val validator: Validator) {
    fun validate(request: CreateAuthorRequest): Mono<CreateAuthorRequest> {
        validator.validate(mapper(request.author))
        return Mono.just(request)
    }

    fun validate(request: UpdateAuthorRequest): Mono<UpdateAuthorRequest> {
        validator.validate(mapper(request.author))
        return Mono.just(request)
    }

    private fun mapper(author: Author): AuthorDto {
        return with(author) {
            AuthorDto(
                id = id,
                firstName = firstName,
                lastName = lastName,
            )
        }
    }
}

