package com.example.librarymanagement.bookpresence.application.service

import com.example.librarymanagement.bookpresence.application.port.`in`.ExistsByBookIdAndLibraryIdInPort
import com.example.librarymanagement.bookpresence.application.port.out.BookPresenceRepositoryOutPort
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ExistsByBookIdAndLibraryIdUseCase(
    private val bookPresenceRepository: BookPresenceRepositoryOutPort
) : ExistsByBookIdAndLibraryIdInPort {
    override fun existsBookPresenceByBookIdAndLibraryId(bookId: String, libraryId: String): Mono<Boolean> =
        bookPresenceRepository.existsByBookIdAndLibraryId(bookId, libraryId)
}
