package com.example.librarymanagement.book.application.service

import com.example.librarymanagement.book.application.port.`in`.GetBookByTitleAndAuthorInPort
import com.example.librarymanagement.book.application.port.out.BookRepositoryOutPort
import com.example.librarymanagement.book.domain.Book
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class GetBookByTitleAndAuthorUseCase(
    private val bookRepository: BookRepositoryOutPort
) : GetBookByTitleAndAuthorInPort{
    override fun getBookByTitleAndAuthor(title: String, authorId: String): Flux<Book> {
        return bookRepository.findBookByTitleAndAuthorId(title, authorId)
    }
}
