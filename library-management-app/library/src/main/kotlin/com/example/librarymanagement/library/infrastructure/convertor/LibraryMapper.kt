package com.example.librarymanagement.library.infrastructure.convertor

import com.example.internalapi.request.library.create.proto.CreateLibraryRequest
import com.example.internalapi.request.library.update.proto.UpdateLibraryRequest
import com.example.librarymanagement.library.domain.Library
import com.example.librarymanagement.library.infrastructure.entity.MongoLibrary
import org.bson.types.ObjectId
import org.springframework.stereotype.Component

@Component
class LibraryMapper {
    fun toLibrary(request: CreateLibraryRequest): Library {
        return Library(
            id = request.library.id.ifEmpty { null },
            name = request.library.name,
            address = request.library.address
        )
    }

    fun toLibrary(request: UpdateLibraryRequest): Library {
        return Library(
            id = request.library.id.ifEmpty { null },
            name = request.library.name,
            address = request.library.address
        )
    }

    fun toLibraryProto(library: Library): com.example.internalapi.model.Library {
        return com.example.internalapi.model.Library.newBuilder()
            .setId(library.id)
            .setName(library.name)
            .build()
    }

    fun toEntity(library: Library): MongoLibrary {
        return MongoLibrary(
            id = library.id?.let { ObjectId(it) },
            name = library.name,
            address = library.address
        )
    }

    fun toDomain(mongoLibrary: MongoLibrary): Library {
        return Library(
            id = mongoLibrary.id.toString(),
            name = mongoLibrary.name,
            address = mongoLibrary.address
        )
    }
}
