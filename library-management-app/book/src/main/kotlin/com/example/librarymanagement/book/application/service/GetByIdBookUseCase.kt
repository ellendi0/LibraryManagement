package com.example.librarymanagement.book.application.service

import com.example.librarymanagement.book.application.port.`in`.GetByIdBookInPort
import com.example.librarymanagement.book.application.port.out.BookRepositoryOutPort
import com.example.librarymanagement.book.domain.Book
import com.example.librarymanagement.core.application.exception.EntityNotFoundException
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class GetByIdBookUseCase(
    private val bookRepository: BookRepositoryOutPort
) : GetByIdBookInPort {
    override fun getBookById(id: String): Mono<Book> =
        bookRepository
            .findById(id)
            .switchIfEmpty(Mono.error(EntityNotFoundException("Book")))

}
