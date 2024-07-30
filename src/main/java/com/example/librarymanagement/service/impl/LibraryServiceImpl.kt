package com.example.librarymanagement.service.impl

import com.example.librarymanagement.exception.EntityNotFoundException
import com.example.librarymanagement.model.entity.BookPresence
import com.example.librarymanagement.model.entity.Library
import com.example.librarymanagement.model.enums.Availability
import com.example.librarymanagement.repository.LibraryRepository
import com.example.librarymanagement.service.BookPresenceService
import com.example.librarymanagement.service.LibraryService
import com.example.librarymanagement.service.ReservationService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class LibraryServiceImpl(
    private val libraryRepository: LibraryRepository,
    private val bookPresenceService: BookPresenceService,
    private val reservationService: ReservationService
) : LibraryService {

    override fun findAll(): List<Library> = libraryRepository.findAll()

    override fun getLibraryById(id: Long): Library {
        return libraryRepository.findById(id).orElseThrow { throw EntityNotFoundException("Library") }
    }

    override fun createLibrary(library: Library): Library = libraryRepository.save(library)

    override fun updateLibrary(id: Long, updatedLibrary: Library): Library {
        val library = getLibraryById(id).apply {
            this.name = updatedLibrary.name
            this.address = updatedLibrary.address
        }
        return libraryRepository.save(library)
    }

    override fun getAllBooksByLibraryIdAndAvailability(libraryId: Long, availability: Availability): List<BookPresence> {
        return bookPresenceService.getAllBookByLibraryIdAndAvailability(libraryId, availability)
    }

    override fun getAllBooksByLibraryId(libraryId: Long): List<BookPresence> {
        return bookPresenceService.getByLibraryId(libraryId)
    }

    @Transactional
    override fun deleteLibrary(id: Long) {
        libraryRepository.findById(id).ifPresent { library ->
            library.bookPresence.takeIf { it.isNotEmpty() }?.forEach { bookPresence ->
                bookPresenceService.deleteBookPresenceById(bookPresence.id!!)
            }
            reservationService.getReservationsByLibraryId(id).takeIf { it.isNotEmpty() }?.forEach { reservation ->
                reservationService.deleteReservationById(reservation.id!!)
            }
            libraryRepository.deleteById(id)
        }
    }
}