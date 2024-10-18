package com.example.librarymanagement.bookpresence.application.service

import com.example.librarymanagement.bookpresence.application.port.`in`.FindAllBookPresenceByLibraryIdInPort
import com.example.librarymanagement.bookpresence.application.port.out.BookPresenceRepositoryOutPort
import com.example.librarymanagement.bookpresence.domain.BookPresence
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class FindAllBookPresenceByLibraryIdUseCase(
    private val bookPresenceRepository: BookPresenceRepositoryOutPort
) : FindAllBookPresenceByLibraryIdInPort {
    override fun findAllByLibraryId(libraryId: String): Flux<BookPresence> =
        bookPresenceRepository.findAllByLibraryId(libraryId)
}
