package com.example.librarymanagement.dto.mapper.nats

import com.example.internalapi.request.library.create.proto.CreateLibraryRequest
import com.example.internalapi.request.library.update.proto.UpdateLibraryRequest
import com.example.librarymanagement.model.domain.Library
import org.springframework.stereotype.Component
import com.example.internalapi.model.Library as LibraryProto

@Component("natsLibraryMapper")
class LibraryMapper {
    fun toLibrary(request: CreateLibraryRequest, id: String? = null): Library {
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

    fun toLibraryProto(library: Library): LibraryProto {
        return LibraryProto.newBuilder()
            .setId(library.id)
            .setName(library.name)
            .build()
    }
}
