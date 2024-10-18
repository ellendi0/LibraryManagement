package com.example.librarymanagement.book.application.service

import com.example.librarymanagement.author.application.port.`in`.GetAuthorByIdInPort
import com.example.librarymanagement.book.application.port.`in`.CreateBookInPort
import com.example.librarymanagement.book.application.port.out.BookRepositoryOutPort
import com.example.librarymanagement.book.domain.Book
import com.example.librarymanagement.core.application.exception.DuplicateKeyException
import com.example.librarymanagement.publisher.application.port.`in`.GetPublisherByIdInPort
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class CreateBookUseCase(
    private val bookRepository: BookRepositoryOutPort,
    private val getAuthorByIdInPort: GetAuthorByIdInPort,
    private val getPublisherByIdInPort: GetPublisherByIdInPort,
) : CreateBookInPort {
    override fun createBook(book: Book): Mono<Book> {
        return validate(book).flatMap { bookRepository.save(book) }
    }

    private fun validate(book: Book): Mono<Boolean> {
        return Mono.zip(
            bookRepository.existsByIsbn(book.isbn),
            getAuthorByIdInPort.getAuthorById(book.authorId),
            getPublisherByIdInPort.getPublisherById(book.publisherId)
        ).flatMap { if (it.t1 == true) Mono.error(DuplicateKeyException("Book", "isbn")) else Mono.just(false) }
    }
}
