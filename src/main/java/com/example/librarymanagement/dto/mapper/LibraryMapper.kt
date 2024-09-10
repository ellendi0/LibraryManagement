package com.example.librarymanagement.dto.mapper

import com.example.librarymanagement.dto.LibraryDto
import com.example.librarymanagement.model.domain.Library
import org.springframework.stereotype.Component

@Component
class LibraryMapper {
    fun toLibrary(libraryDto: LibraryDto, id: String? = null): Library {
        return Library(id = libraryDto.id ?: id, libraryDto.name, address = libraryDto.address)
    }

    fun toLibraryDto(library: Library): LibraryDto = LibraryDto(library.id!!, library.name, library.address)

    fun toLibraryDto(libraries: List<Library>): List<LibraryDto> = libraries.map { toLibraryDto(it) }
}
