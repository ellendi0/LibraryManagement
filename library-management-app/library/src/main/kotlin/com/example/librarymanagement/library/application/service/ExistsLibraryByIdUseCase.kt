package com.example.librarymanagement.library.application.service

import com.example.librarymanagement.core.application.exception.EntityNotFoundException
import com.example.librarymanagement.library.application.port.`in`.ExistsLibraryByIdInPort
import com.example.librarymanagement.library.application.port.out.LibraryRepositoryOutPort
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ExistsLibraryByIdUseCase(private val libraryRepository: LibraryRepositoryOutPort) : ExistsLibraryByIdInPort {
    override fun existsLibraryById(id: String): Mono<Boolean> =
        libraryRepository.existsById(id)
            .flatMap { if (it == false) Mono.error(EntityNotFoundException("Library")) else Mono.just(it) }
}
