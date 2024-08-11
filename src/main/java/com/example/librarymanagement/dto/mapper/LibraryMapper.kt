package com.example.librarymanagement.dto.mapper

import com.example.librarymanagement.dto.LibraryDto
import com.example.librarymanagement.model.entity.Library
import org.springframework.stereotype.Component

@Component
class LibraryMapper {
    fun toLibrary(libraryDto: LibraryDto, id: Long? = null): Library{
        return Library(id = id, libraryDto.name, address = libraryDto.address)
    }

    fun toLibraryDto(library: Library): LibraryDto = LibraryDto(library.id!!, library.name, library.address)

    fun toLibraryDto(libraries: List<Library>): List<LibraryDto> = libraries.map { toLibraryDto(it) }
}
