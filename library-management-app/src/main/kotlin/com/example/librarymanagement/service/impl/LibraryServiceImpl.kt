package com.example.librarymanagement.service.impl

import com.example.librarymanagement.exception.EntityNotFoundException
import com.example.librarymanagement.model.domain.Library
import com.example.librarymanagement.repository.LibraryRepository
import com.example.librarymanagement.service.LibraryService
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class LibraryServiceImpl(
    private val libraryRepository: LibraryRepository,
) : LibraryService {

    override fun getAllLibraries(): Flux<Library> = libraryRepository.findAll()

    override fun getLibraryById(id: String): Mono<Library> {
        return libraryRepository
            .findById(id)
            .switchIfEmpty(Mono.error(EntityNotFoundException("Library")))
    }

    override fun createLibrary(library: Library): Mono<Library> = libraryRepository.save(library)

    override fun updateLibrary(updatedLibrary: Library): Mono<Library> {
        return getLibraryById(updatedLibrary.id!!)
            .map { it.copy( name = updatedLibrary.name, address = updatedLibrary.address ) }
            .flatMap { libraryRepository.save(it) }
    }

    override fun deleteLibraryById(id: String): Mono<Unit> = libraryRepository.deleteById(id)

    override fun existsLibraryById(id: String): Mono<Boolean> =
        libraryRepository.existsById(id)
            .flatMap { if (it == false) Mono.error(EntityNotFoundException("Library")) else Mono.just(it) }
}
