package com.example.librarymanagement.book.application.service

import com.example.librarymanagement.book.application.port.`in`.DeleteBookByIdInPort
import com.example.librarymanagement.book.application.port.out.BookRepositoryOutPort
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class DeleteBookByIdUseCase(
    private val bookRepository: BookRepositoryOutPort
) : DeleteBookByIdInPort {
    override fun deleteBookById(id: String): Mono<Unit> = bookRepository.deleteById(id)
}
