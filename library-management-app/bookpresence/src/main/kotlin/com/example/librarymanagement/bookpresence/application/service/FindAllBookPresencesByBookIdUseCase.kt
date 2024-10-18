package com.example.librarymanagement.bookpresence.application.service

import com.example.librarymanagement.bookpresence.application.port.`in`.FindAllBookPresencesByBookIdInPort
import com.example.librarymanagement.bookpresence.application.port.out.BookPresenceRepositoryOutPort
import com.example.librarymanagement.bookpresence.domain.BookPresence
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class FindAllBookPresencesByBookIdUseCase(
    private val bookPresenceRepository: BookPresenceRepositoryOutPort
) : FindAllBookPresencesByBookIdInPort {
    override fun findAllByBookId(bookId: String): Flux<BookPresence> = bookPresenceRepository.findAllByBookId(bookId)
}
