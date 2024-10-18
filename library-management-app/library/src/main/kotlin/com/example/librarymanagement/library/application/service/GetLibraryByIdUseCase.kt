package com.example.librarymanagement.library.application.service

import com.example.librarymanagement.core.application.exception.EntityNotFoundException
import com.example.librarymanagement.library.application.port.`in`.GetLibraryByIdInPort
import com.example.librarymanagement.library.application.port.out.LibraryRepositoryOutPort
import com.example.librarymanagement.library.domain.Library
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class GetLibraryByIdUseCase(private val libraryRepository: LibraryRepositoryOutPort) : GetLibraryByIdInPort {
    override fun getLibraryById(id: String): Mono<Library> {
        return libraryRepository
            .findById(id)
            .switchIfEmpty(Mono.error(EntityNotFoundException("Library")))
    }
}
