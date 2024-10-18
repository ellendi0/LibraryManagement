package com.example.librarymanagement.library.application.service

import com.example.librarymanagement.core.application.exception.EntityNotFoundException
import com.example.librarymanagement.library.application.port.`in`.ExistsLibraryWithAvailableBookInPort
import com.example.librarymanagement.library.application.port.out.LibraryRepositoryOutPort
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ExistsLibraryWithAvailableBookUseCase(
    private val libraryRepository: LibraryRepositoryOutPort
) : ExistsLibraryWithAvailableBookInPort {
    override fun existsLibraryWithAvailableBook(bookId: String, libraryId: String): Mono<Boolean> =
        libraryRepository.existsLibraryWithAvailableBook(bookId, libraryId)
            .flatMap { if (it == false) Mono.error(EntityNotFoundException("Presence of book")) else Mono.just(it) }
}
