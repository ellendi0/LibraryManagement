package com.example.librarymanagement.service.impl

import com.example.librarymanagement.exception.DuplicateKeyException
import com.example.librarymanagement.exception.EntityNotFoundException
import com.example.librarymanagement.model.domain.Book
import com.example.librarymanagement.repository.BookRepository
import com.example.librarymanagement.service.AuthorService
import com.example.librarymanagement.service.BookService
import com.example.librarymanagement.service.PublisherService
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class BookServiceImpl(
    private val bookRepository: BookRepository,
    private val authorService: AuthorService,
    private val publisherService: PublisherService
) : BookService {

    override fun getAll(): Flux<Book> = bookRepository.findAll()

    override fun getBookById(id: String): Mono<Book> =
        bookRepository
            .findById(id)
            .switchIfEmpty(Mono.error(EntityNotFoundException("Book")))

    override fun getBookByTitleAndAuthor(title: String, authorId: String): Flux<Book> {
        return bookRepository.findBookByTitleAndAuthorId(title, authorId)
    }

    override fun createBook(book: Book): Mono<Book> {
        return validate(book).flatMap { bookRepository.save(book) }
    }

    override fun updateBook(updatedBook: Book): Mono<Book> {
        return validate(updatedBook)
            .flatMap { getBookById(updatedBook.id!!) }
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

    override fun deleteBookById(id: String): Mono<Unit> = bookRepository.deleteById(id)

    override fun existsBookById(id: String): Mono<Boolean> =
        bookRepository.existsById(id)
            .flatMap { if (it == false) Mono.error(EntityNotFoundException("Book")) else Mono.just(it) }

    private fun validate(book: Book): Mono<Boolean> {
        return Mono.zip(
            bookRepository.existsByIsbn(book.isbn),
            authorService.getAuthorById(book.authorId),
            publisherService.getPublisherById(book.publisherId)
        ).flatMap { if (it.t1 == true) Mono.error(DuplicateKeyException("Book", "isbn")) else Mono.just(false) }
    }
}
