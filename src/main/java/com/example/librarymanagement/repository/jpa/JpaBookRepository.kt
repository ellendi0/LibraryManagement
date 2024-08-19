package com.example.librarymanagement.repository.jpa

import com.example.librarymanagement.model.domain.Book
import com.example.librarymanagement.model.jpa.JpaBook
import com.example.librarymanagement.repository.BookRepository
import com.example.librarymanagement.repository.jpa.mapper.JpaBookMapper
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class JpaBookRepository(
    private val bookRepository: BookRepositorySpring,
    private val bookPresenceRepository: BookPresenceRepositorySpring,
    private val reservationRepository: ReservationRepositorySpring
): BookRepository{
    private fun Book.toEntity() = JpaBookMapper.toEntity(this)
    private fun JpaBook.toDomain() = JpaBookMapper.toDomain(this)

    override fun save(book: Book): Book {
        return bookRepository.save(book.toEntity()).toDomain()
    }

    override fun findById(bookId: String): Book? {
        return bookRepository.findByIdOrNull(bookId.toLong())?.toDomain()
    }

    override fun findAll(): List<Book> {
        return bookRepository.findAll().map { it.toDomain() }
    }

    @Transactional
    override fun delete(bookId: String) {
        bookRepository.findByIdOrNull(bookId.toLong())?.let { book ->

            book.bookPresence
                .takeIf { it.isNotEmpty() }
                ?.forEach { bookPresence ->
                    bookPresence.id?.let { bookPresenceRepository.deleteById(it) }
            }

            book.reservations
                .takeIf { it.isNotEmpty() }
                ?.forEach { reservation ->
                    reservation.id?.let { reservationRepository.deleteById(it) }
            }

            bookRepository.deleteById(bookId.toLong())
        }
    }

    override fun existsByIsbn(isbn: Long): Boolean {
        return bookRepository.existsById(isbn)
    }

    override fun findBookByTitleAndAuthorId(title: String, authorId: String): Book? {
        return bookRepository.findBookByTitleAndAuthorId(title, authorId.toLong())
    }
}

@Repository
interface BookRepositorySpring: JpaRepository<JpaBook, Long>{
    fun findBookByTitleAndAuthorId(title: String, id: Long): Book?
}
