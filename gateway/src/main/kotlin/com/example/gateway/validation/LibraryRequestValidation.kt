package com.example.com.example.gateway.validation

import com.example.com.example.gateway.validation.model.LibraryDto
import com.example.internalapi.model.Library
import com.example.internalapi.request.library.create.proto.CreateLibraryRequest
import com.example.internalapi.request.library.update.proto.UpdateLibraryRequest
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class LibraryRequestValidation(private val validator: Validator) {
    fun validate(request: CreateLibraryRequest): Mono<CreateLibraryRequest> {
        validator.validate(mapper(request.library))
        return Mono.just(request)
    }

    fun validate(request: UpdateLibraryRequest): Mono<UpdateLibraryRequest> {
        validator.validate(mapper(request.library))
        return Mono.just(request)
    }

    private fun mapper(library: Library): LibraryDto {
        return with(library) {
            LibraryDto(
                id = id,
                name = name,
                address = address
            )
        }
    }

}
