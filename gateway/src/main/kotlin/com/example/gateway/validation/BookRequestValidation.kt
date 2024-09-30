package com.example.com.example.gateway.validation

import com.example.com.example.gateway.validation.model.BookDto
import com.example.internalapi.model.Book
import com.example.internalapi.request.book.create.proto.CreateBookRequest
import com.example.internalapi.request.book.update.proto.UpdateBookRequest
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class BookRequestValidation(private val validator: Validator) {
    fun validate(request: CreateBookRequest): Mono<CreateBookRequest> {
        validator.validate(mapper(request.book))
        return Mono.just(request)
    }

    fun validate(request: UpdateBookRequest): Mono<UpdateBookRequest> {
        validator.validate(mapper(request.book))
        return Mono.just(request)
    }

    private fun mapper(book: Book): BookDto {
        return with(book) {
            BookDto(
                id = id,
                title = title,
                publishedYear = publishedYear,
                isbn = isbn,
                genre = genre.toString(),
                authorId = authorId,
                publisherId = publisherId,
            )
        }
    }
}
