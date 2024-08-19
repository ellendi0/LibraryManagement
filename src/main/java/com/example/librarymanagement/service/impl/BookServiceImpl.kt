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
    private val publisherService: PublisherService,
) : BookService {

    override fun findAll(): List<Book> = bookRepository.findAll()

    override fun getBookById(id: String): Book =
        bookRepository.findById(id) ?: throw EntityNotFoundException("Book")

    override fun getBookByTitleAndAuthor(title: String, authorId: String): Book {
        return bookRepository.findBookByTitleAndAuthorId(title, authorId) ?: throw EntityNotFoundException("Book")
    }

    override fun createBook(authorId: String, publisherId: String, book: Book): Book {
        if (bookRepository.existsByIsbn(book.isbn)) throw DuplicateKeyException("Book", "ISBN")
        book.author = authorService.getAuthorById(authorId)
        book.publisher = publisherService.getPublisherById(publisherId)
        return bookRepository.save(book)
    }

    override fun updateBook(updatedBook: Book): Book {
        val book = getBookById(updatedBook.id!!).copy(
            title = updatedBook.title,
            isbn = updatedBook.isbn,
            publishedYear = updatedBook.publishedYear,
            genre = updatedBook.genre
        )
        return bookRepository.save(book)
    }

    override fun deleteBook(id: String) {
        return bookRepository.delete(id)
    }
}
