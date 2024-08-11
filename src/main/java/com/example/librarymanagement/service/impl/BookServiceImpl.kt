package com.example.librarymanagement.service.impl

import com.example.librarymanagement.exception.DuplicateKeyException
import com.example.librarymanagement.exception.EntityNotFoundException
import com.example.librarymanagement.model.entity.Book
import com.example.librarymanagement.repository.BookRepository
import com.example.librarymanagement.service.AuthorService
import com.example.librarymanagement.service.BookPresenceService
import com.example.librarymanagement.service.BookService
import com.example.librarymanagement.service.PublisherService
import com.example.librarymanagement.service.ReservationService
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class BookServiceImpl(
    private val bookRepository: BookRepository,
    private val authorService: AuthorService,
    private val publisherService: PublisherService,
    private val bookPresenceService: BookPresenceService,
    private val reservationService: ReservationService
) : BookService {

    override fun findAll(): List<Book> = bookRepository.findAll()

    override fun getBookById(id: Long): Book =
        bookRepository.findByIdOrNull(id) ?: throw EntityNotFoundException("Book")

    override fun getBookByTitleAndAuthor(title: String, authorId: Long): Book {
        return bookRepository.findBookByTitleAndAuthorId(title, authorId) ?: throw EntityNotFoundException("Book")
    }

    override fun createBook(authorId: Long, publisherId: Long, book: Book): Book {
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

    @Transactional
    override fun deleteBook(id: Long) {
        bookRepository.findById(id).ifPresent { book ->
            book.bookPresence.takeIf { it.isNotEmpty() }?.forEach { bookPresence ->
                bookPresenceService.deleteBookPresenceById(bookPresence.id!!)
            }
            book.reservations.takeIf { it.isNotEmpty() }?.forEach { reservation ->
                reservationService.deleteReservationById(reservation.id!!)
            }
            bookRepository.deleteById(id)
        }
    }
}
