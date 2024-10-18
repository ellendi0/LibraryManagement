package com.example.librarymanagement.book.application.service

import com.example.librarymanagement.author.application.port.`in`.GetAuthorByIdInPort
import com.example.librarymanagement.book.application.port.`in`.GetByIdBookInPort
import com.example.librarymanagement.book.application.port.`in`.UpdateBookInPort
import com.example.librarymanagement.book.application.port.out.BookRepositoryOutPort
import com.example.librarymanagement.book.domain.Book
import com.example.librarymanagement.core.application.exception.DuplicateKeyException
import com.example.librarymanagement.publisher.application.port.`in`.GetPublisherByIdInPort
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UpdateBookUseCase(
    private val bookRepository: BookRepositoryOutPort,
    private val getBookByIdInPort: GetByIdBookInPort,
    private val getAuthorByIdInPort: GetAuthorByIdInPort,
    private val getPublisherByIdInPort: GetPublisherByIdInPort
) : UpdateBookInPort {
    override fun updateBook(updatedBook: Book): Mono<Book> {
        return validate(updatedBook)
            .flatMap { getBookByIdInPort.getBookById(updatedBook.id!!) }
            .flatMap {
                bookRepository.save(
                    it.copy(
                        title = updatedBook.title,
                        authorId = updatedBook.authorId,
                        publisherId = updatedBook.publisherId,
                        isbn = updatedBook.isbn,
                        publishedYear = updatedBook.publishedYear,
                        genre = updatedBook.genre
                    )
                )
            }
    }

    private fun validate(book: Book): Mono<Boolean> {
        return Mono.zip(
            bookRepository.existsByIsbn(book.isbn),
            getAuthorByIdInPort.getAuthorById(book.authorId),
            getPublisherByIdInPort.getPublisherById(book.publisherId)
        ).flatMap { if (it.t1 == true) Mono.error(DuplicateKeyException("Book", "isbn")) else Mono.just(false) }
    }
}
