package com.example.librarymanagement.bookpresence.application.service

import com.example.librarymanagement.bookpresence.application.port.`in`.RemoveBookPresenceFromLibraryInPort
import com.example.librarymanagement.bookpresence.application.port.out.BookPresenceRepositoryOutPort
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class RemoveBookPresenceFromLibraryUseCase(
    private val bookPresenceRepository: BookPresenceRepositoryOutPort
) : RemoveBookPresenceFromLibraryInPort {
    override fun deleteBookPresenceById(id: String): Mono<Unit> = bookPresenceRepository.deleteById(id)
}
