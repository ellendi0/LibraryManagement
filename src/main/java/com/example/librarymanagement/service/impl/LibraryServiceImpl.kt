package com.example.librarymanagement.service.impl

import com.example.librarymanagement.exception.EntityNotFoundException
import com.example.librarymanagement.model.domain.BookPresence
import com.example.librarymanagement.model.domain.Library
import com.example.librarymanagement.model.enums.Availability
import com.example.librarymanagement.repository.LibraryRepository
import com.example.librarymanagement.service.BookPresenceService
import com.example.librarymanagement.service.LibraryService
import org.springframework.stereotype.Service

@Service
class LibraryServiceImpl(
    private val libraryRepository: LibraryRepository,
    private val bookPresenceService: BookPresenceService,
) : LibraryService {

    override fun findAll(): List<Library> = libraryRepository.findAll()

    override fun getLibraryById(id: String): Library {
        return libraryRepository.findById(id) ?: throw EntityNotFoundException("Library")
    }

    override fun createLibrary(library: Library): Library = libraryRepository.save(library)

    override fun updateLibrary(updatedLibrary: Library): Library {
        val library = getLibraryById(updatedLibrary.id!!).copy(
            name = updatedLibrary.name,
            address = updatedLibrary.address
        )
        return libraryRepository.save(library)
    }

    override fun getAllBooksByLibraryIdAndAvailability(
            libraryId: String,
            availability: Availability
    ): List<BookPresence> {
        return bookPresenceService.getAllBookByLibraryIdAndAvailability(libraryId, availability)
    }

    override fun getAllBooksByLibraryId(libraryId: String): List<BookPresence> {
        return bookPresenceService.getByLibraryId(libraryId)
    }

    override fun deleteLibrary(id: String) {
        return libraryRepository.delete(id)
    }
}
