package com.example.librarymanagement.library.application.service

import com.example.librarymanagement.library.application.port.`in`.CreateLibraryInPort
import com.example.librarymanagement.library.application.port.out.LibraryRepositoryOutPort
import com.example.librarymanagement.library.domain.Library
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class CreateLibraryUseCase(private val libraryRepository: LibraryRepositoryOutPort) : CreateLibraryInPort {
    override fun createLibrary(library: Library): Mono<Library> = libraryRepository.save(library)
}
