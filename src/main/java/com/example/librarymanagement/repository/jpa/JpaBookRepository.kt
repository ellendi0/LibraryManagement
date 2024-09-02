package com.example.librarymanagement.repository.jpa

import com.example.librarymanagement.model.domain.Book
import com.example.librarymanagement.model.jpa.JpaBook
import com.example.librarymanagement.repository.BookRepository
import com.example.librarymanagement.repository.jpa.mapper.JpaAuthorMapper
import com.example.librarymanagement.repository.jpa.mapper.JpaBookMapper
import com.example.librarymanagement.repository.jpa.mapper.JpaPublisherMapper
import jakarta.transaction.Transactional
import org.springframework.context.annotation.Profile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
@Profile("jpa")
class JpaBookRepository(
    private val bookRepository: BookRepositorySpring,
    private val authorRepository: JpaAuthorRepository,
    private val publisherRepository: JpaPublisherRepository,
) : BookRepository {
    private fun Book.toEntity() = JpaBookMapper.toEntity(this)
    private fun JpaBook.toDomain() = JpaBookMapper.toDomain(this)

    override fun save(book: Book): Book {
        return bookRepository.save(
            book.toEntity().copy(
                author = JpaAuthorMapper.toEntity(authorRepository.findById(book.authorId)!!),
                publisher = JpaPublisherMapper.toEntity(publisherRepository.findById(book.publisherId)!!)
            )
        ).toDomain()
    }

    override fun findById(bookId: String): Book? {
        return bookRepository.findByIdOrNull(bookId.toLong())?.toDomain()
    }

    override fun findAll(): List<Book> {
        return bookRepository.findAll().map { it.toDomain() }
    }

    @Transactional
    override fun deleteById(bookId: String) {
        bookRepository.deleteById(bookId.toLong())
    }

    override fun existsByIsbn(isbn: Long): Boolean {
        return bookRepository.existsById(isbn)
    }

    override fun findBookByTitleAndAuthorId(title: String, authorId: String): Book? {
        return bookRepository.findBookByTitleAndAuthorId(title, authorId.toLong())?.toDomain()
    }
}

@Repository
interface BookRepositorySpring : JpaRepository<JpaBook, Long> {
    fun findBookByTitleAndAuthorId(title: String, id: Long): JpaBook?
}
