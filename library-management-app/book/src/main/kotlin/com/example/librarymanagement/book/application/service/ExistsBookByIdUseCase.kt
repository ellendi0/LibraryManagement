package com.example.librarymanagement.book.application.service

import com.example.librarymanagement.book.application.port.`in`.ExistsBookByIdInPort
import com.example.librarymanagement.book.application.port.out.BookRepositoryOutPort
import com.example.librarymanagement.core.application.exception.EntityNotFoundException
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ExistsBookByIdUseCase(
    private val bookRepository: BookRepositoryOutPort
): ExistsBookByIdInPort {
    override fun existsBookById(id: String): Mono<Boolean> =
        bookRepository.existsById(id)
            .flatMap { if (it == false) Mono.error(EntityNotFoundException("Book")) else Mono.just(it) }

}
