package com.example.librarymanagement.service.impl

import com.example.librarymanagement.exception.DuplicateKeyException
import com.example.librarymanagement.exception.EntityNotFoundException
import com.example.librarymanagement.model.domain.Book
import com.example.librarymanagement.repository.BookRepository
import com.example.librarymanagement.service.AuthorService
import com.example.librarymanagement.service.BookService
import com.example.librarymanagement.service.PublisherService
import org.springframework.stereotype.Service

@Service
class BookServiceImpl(
    private val bookRepository: BookRepository,
    private val authorService: AuthorService,
    private val publisherService: PublisherService
) : BookService {

    override fun findAll(): List<Book> = bookRepository.findAll()

    override fun getBookById(id: String): Book =
        bookRepository.findById(id) ?: throw EntityNotFoundException("Book")

    override fun getBookByTitleAndAuthor(title: String, authorId: String): Book {
        return bookRepository.findBookByTitleAndAuthorId(title, authorId) ?: throw EntityNotFoundException("Book")
    }

    override fun createBook(book: Book): Book {
        if (bookRepository.existsByIsbn(book.isbn)) throw DuplicateKeyException("Book", "ISBN")
        authorService.getAuthorById(book.authorId)
        publisherService.getPublisherById(book.publisherId)

        return bookRepository.save(book)
    }

    override fun updateBook(updatedBook: Book): Book {
        authorService.getAuthorById(updatedBook.authorId)
        publisherService.getPublisherById(updatedBook.publisherId)

        val book = getBookById(updatedBook.id!!)
        if (bookRepository.existsByIsbn(updatedBook.isbn) && updatedBook.isbn != book.isbn)
            throw DuplicateKeyException("Book", "ISBN")

        return bookRepository.save(
            book.copy(
                title = updatedBook.title,
                authorId = updatedBook.authorId,
                publisherId = updatedBook.publisherId,
                isbn = updatedBook.isbn,
                publishedYear = updatedBook.publishedYear,
                genre = updatedBook.genre
            )
        )
    }

    override fun deleteBookById(id: String) {
        return bookRepository.deleteById(id)
    }
}
