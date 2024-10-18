package com.example.librarymanagement.bookpresence.application.service

import com.example.librarymanagement.book.application.port.`in`.ExistsBookByIdInPort
import com.example.librarymanagement.bookpresence.application.port.`in`.AddBookToLibraryInPort
import com.example.librarymanagement.bookpresence.application.port.out.BookPresenceRepositoryOutPort
import com.example.librarymanagement.bookpresence.domain.BookPresence
import com.example.librarymanagement.library.application.port.`in`.ExistsLibraryByIdInPort
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AddBookToLibraryUseCase(
    private val bookPresenceRepository: BookPresenceRepositoryOutPort,
    private val existsLibraryByIdInPort: ExistsLibraryByIdInPort,
    private val existsBookByIdInPort: ExistsBookByIdInPort,
) : AddBookToLibraryInPort {
    override fun addBookToLibrary(libraryId: String, bookId: String): Mono<BookPresence> {
        return Mono.zip(
            existsLibraryByIdInPort.existsLibraryById(libraryId),
            existsBookByIdInPort.existsBookById(bookId)
        )
            .flatMap {
                val bookPresence = BookPresence(bookId = bookId, libraryId = libraryId)
                bookPresenceRepository.saveOrUpdate(bookPresence)
            }
    }
}
