package com.example.librarymanagement.repository.jpa

import com.example.librarymanagement.model.domain.Book
import com.example.librarymanagement.model.jpa.JpaBook
import com.example.librarymanagement.repository.BookRepository
import com.example.librarymanagement.repository.jpa.mapper.JpaAuthorMapper
import com.example.librarymanagement.repository.jpa.mapper.JpaBookMapper
import com.example.librarymanagement.repository.jpa.mapper.JpaPublisherMapper
import org.springframework.context.annotation.Profile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Repository
@Profile("jpa")
class JpaBookRepository(
    private val bookRepository: BookRepositorySpring,
    private val authorRepository: JpaAuthorRepository,
    private val publisherRepository: JpaPublisherRepository,
) : BookRepository {
    private fun Book.toEntity() = JpaBookMapper.toEntity(this)
    private fun JpaBook.toDomain() = JpaBookMapper.toDomain(this)

    override fun save(book: Book): Mono<Book> {
        return Mono.zip(
            authorRepository.findById(book.authorId),
            publisherRepository.findById(book.publisherId)
        )
            .flatMap {
                val bookEntity = book.toEntity()
                    .copy(author = JpaAuthorMapper.toEntity(it.t1), publisher = JpaPublisherMapper.toEntity(it.t2))
                Mono.fromCallable { bookRepository.save(bookEntity) }.subscribeOn(Schedulers.boundedElastic())
            }
            .map { it.toDomain() }
    }

    override fun findById(bookId: String): Mono<Book> =
        Mono.fromCallable { bookRepository.findByIdOrNull(bookId.toLong())?.toDomain() }

    override fun findAll(): Flux<Book> = Flux.fromIterable(bookRepository.findAll().map { it.toDomain() })

    override fun deleteById(bookId: String): Mono<Unit> =
        Mono.fromCallable { bookRepository.deleteById(bookId.toLong()) }.subscribeOn(Schedulers.boundedElastic())
            .then(Mono.just(Unit))

    override fun existsById(bookId: String): Mono<Boolean> =
        Mono.fromCallable { bookRepository.existsById(bookId.toLong()) }.subscribeOn(Schedulers.boundedElastic())

    override fun existsByIsbn(isbn: Long): Mono<Boolean> =
        Mono.fromCallable { bookRepository.existsById(isbn) }.subscribeOn(Schedulers.boundedElastic())

    override fun findBookByTitleAndAuthorId(title: String, authorId: String): Flux<Book> {
        return Mono.fromCallable { bookRepository.findBookByTitleAndAuthorId(title, authorId.toLong()) }
            .subscribeOn(Schedulers.boundedElastic())
            .flatMapMany { Flux.fromIterable(it.map { book -> book.toDomain() }) }
    }
}

@Repository
interface BookRepositorySpring : JpaRepository<JpaBook, Long> {
    fun findBookByTitleAndAuthorId(title: String, id: Long): List<JpaBook>
}
