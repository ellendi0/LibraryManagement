package com.example.librarymanagement.library.application.service

import com.example.librarymanagement.library.application.port.`in`.GetLibraryByIdInPort
import com.example.librarymanagement.library.application.port.`in`.UpdateLibraryInPort
import com.example.librarymanagement.library.application.port.out.LibraryRepositoryOutPort
import com.example.librarymanagement.library.domain.Library
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UpdateLibraryUseCase(
    private val libraryRepository: LibraryRepositoryOutPort,
    private val getLibraryByIdInPort: GetLibraryByIdInPort
) : UpdateLibraryInPort {
    override fun updateLibrary(updatedLibrary: Library): Mono<Library> {
        return getLibraryByIdInPort.getLibraryById(updatedLibrary.id!!)
            .map { it.copy(name = updatedLibrary.name, address = updatedLibrary.address) }
            .flatMap { libraryRepository.save(it) }
    }
}
