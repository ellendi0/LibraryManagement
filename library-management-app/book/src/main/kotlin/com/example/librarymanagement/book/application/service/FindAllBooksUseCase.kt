package com.example.librarymanagement.book.application.service

import com.example.librarymanagement.book.application.port.`in`.FindAllBooksInPort
import com.example.librarymanagement.book.application.port.out.BookRepositoryOutPort
import com.example.librarymanagement.book.domain.Book
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class FindAllBooksUseCase(
    private val bookRepository: BookRepositoryOutPort
): FindAllBooksInPort {
    override fun findAll(): Flux<Book> = bookRepository.findAll()
}
