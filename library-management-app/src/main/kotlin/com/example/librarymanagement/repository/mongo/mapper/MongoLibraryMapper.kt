package com.example.librarymanagement.repository.mongo.mapper

import com.example.librarymanagement.model.domain.Library
import com.example.librarymanagement.model.mongo.MongoLibrary
import org.bson.types.ObjectId

object MongoLibraryMapper {
    fun toEntity(library: Library): MongoLibrary {
        return MongoLibrary(
                id = library.id?.let {ObjectId(it)},
                name = library.name,
                address = library.address
        )
    }

    fun toDomain(mongoLibrary: MongoLibrary): Library{
        return Library(
                id = mongoLibrary.id.toString(),
                name = mongoLibrary.name,
                address = mongoLibrary.address
        )
    }
}
