package com.example.librarymanagement.library.application.service

import com.example.librarymanagement.library.application.port.`in`.DeleteLibraryByIdInPort
import com.example.librarymanagement.library.application.port.out.LibraryRepositoryOutPort
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class DeleteLibraryByIdUseCase(private val libraryRepository: LibraryRepositoryOutPort) : DeleteLibraryByIdInPort {
    override fun deleteLibraryById(id: String): Mono<Unit> = libraryRepository.deleteById(id)
}
