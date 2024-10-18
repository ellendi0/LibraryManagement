package com.example.librarymanagement.bookpresence.application.service

import com.example.librarymanagement.bookpresence.application.port.`in`.ExistsAvailableBookInLibraryInPort
import com.example.librarymanagement.bookpresence.application.port.`in`.FindAllBookPresenceByLibraryIdAndBookIdInPort
import com.example.librarymanagement.bookpresence.domain.Availability
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ExistsAvailableBookInLibraryUseCase(
    private val findAllBookPresenceByLibraryIdAndBookIdInPort: FindAllBookPresenceByLibraryIdAndBookIdInPort
) : ExistsAvailableBookInLibraryInPort {
    override fun existsAvailableBookInLibrary(bookId: String, libraryId: String): Mono<Boolean> {
        return findAllBookPresenceByLibraryIdAndBookIdInPort.findAllBookPresencesByLibraryIdAndBookId(
            libraryId,
            bookId
        ).filter { it.availability == Availability.AVAILABLE }.hasElements()
    }
}
