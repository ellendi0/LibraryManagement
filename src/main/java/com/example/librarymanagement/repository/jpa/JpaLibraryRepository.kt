package com.example.librarymanagement.repository.jpa

import com.example.librarymanagement.model.domain.Library
import com.example.librarymanagement.model.jpa.JpaLibrary
import com.example.librarymanagement.repository.LibraryRepository
import com.example.librarymanagement.repository.jpa.mapper.JpaLibraryMapper
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class JpaLibraryRepository(
    private val libraryRepository: LibraryRepositorySpring,
    private val bookPresenceRepository: BookPresenceRepositorySpring,
    private val reservationRepository: ReservationRepositorySpring
) : LibraryRepository{
    private fun Library.toEntity() = JpaLibraryMapper.toEntity(this)
    private fun JpaLibrary.toDomain() = JpaLibraryMapper.toDomain(this)

    override fun save(library: Library): Library {
        return libraryRepository.save(library.toEntity()).toDomain()
    }

    override fun findById(libraryId: String): Library? {
        return libraryRepository.findByIdOrNull(libraryId.toLong())?.toDomain()
    }

    override fun findAll(): List<Library> {
        return libraryRepository.findAll().map { it.toDomain() }
    }

    @Transactional
    override fun delete(libraryId: String) {
        libraryRepository.findByIdOrNull(libraryId.toLong())?.let { library ->

            library.bookPresence
                .takeIf { it.isNotEmpty() }
                ?.forEach { bookPresence ->
                    bookPresence.id?.let { bookPresenceRepository.deleteById(it) }
            }

            reservationRepository.findAllByLibraryId(libraryId.toLong())
                .takeIf { it.isNotEmpty() }
                ?.forEach { reservation ->
                    reservation.id?.let { reservationRepository.deleteById(it) }
            }

            libraryRepository.deleteById(libraryId.toLong())
        }
    }
}

@Repository
interface LibraryRepositorySpring: JpaRepository<JpaLibrary, Long>
