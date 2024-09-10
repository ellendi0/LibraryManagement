package com.example.librarymanagement.repository.jpa.mapper

import com.example.librarymanagement.model.domain.Library
import com.example.librarymanagement.model.jpa.JpaLibrary

object JpaLibraryMapper {
    fun toEntity(library: Library): JpaLibrary {
        return JpaLibrary(
            id = library.id?.toLong(),
            name = library.name,
            address = library.address
        )
    }

    fun toDomain(jpaLibrary: JpaLibrary): Library {
        return Library(
            id = jpaLibrary.id.toString(),
            name = jpaLibrary.name,
            address = jpaLibrary.address
        )
    }
}
