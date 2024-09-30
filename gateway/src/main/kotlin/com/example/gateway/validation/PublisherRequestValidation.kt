package com.example.com.example.gateway.validation

import com.example.com.example.gateway.validation.model.PublisherDto
import com.example.internalapi.model.Publisher
import com.example.internalapi.request.publisher.create.proto.CreatePublisherRequest
import com.example.internalapi.request.publisher.update.proto.UpdatePublisherRequest
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class PublisherRequestValidation(private val validator: Validator) {
    fun validate(request: CreatePublisherRequest): Mono<CreatePublisherRequest> {
        validator.validate(mapper(request.publisher))
        return Mono.just(request)
    }

    fun validate(request: UpdatePublisherRequest): Mono<UpdatePublisherRequest> {
        validator.validate(mapper(request.publisher))
        return Mono.just(request)
    }

    private fun mapper(publisher: Publisher): PublisherDto {
        return with(publisher) {
            PublisherDto(
                id = id,
                name = name
            )
        }
    }
}

