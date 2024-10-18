package com.example.librarymanagement.bookpresence.application.service

import com.example.librarymanagement.bookpresence.application.port.`in`.FindAllBookPresenceByLibraryIdAndBookIdInPort
import com.example.librarymanagement.bookpresence.application.port.out.BookPresenceRepositoryOutPort
import com.example.librarymanagement.bookpresence.domain.BookPresence
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class FindAllBookPresenceByLibraryIdAndBookIdUseCase(
    private val bookPresenceRepository: BookPresenceRepositoryOutPort
) : FindAllBookPresenceByLibraryIdAndBookIdInPort{
    override fun findAllBookPresencesByLibraryIdAndBookId(libraryId: String, bookId: String): Flux<BookPresence> =
        bookPresenceRepository.findAllByLibraryIdAndBookId(libraryId, bookId)}
