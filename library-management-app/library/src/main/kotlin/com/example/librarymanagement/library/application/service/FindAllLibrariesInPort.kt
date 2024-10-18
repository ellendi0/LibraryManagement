package com.example.librarymanagement.library.application.service

import com.example.librarymanagement.library.application.port.`in`.FindAllLibrariesInPort
import com.example.librarymanagement.library.application.port.out.LibraryRepositoryOutPort
import com.example.librarymanagement.library.domain.Library
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class FindAllLibrariesInPort(private val libraryRepository: LibraryRepositoryOutPort) : FindAllLibrariesInPort {
    override fun findAllLibraries(): Flux<Library> = libraryRepository.findAll()
}
