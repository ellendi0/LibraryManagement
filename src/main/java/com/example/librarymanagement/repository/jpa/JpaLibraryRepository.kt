package com.example.librarymanagement.repository.jpa

import com.example.librarymanagement.model.domain.Library
import com.example.librarymanagement.model.jpa.JpaLibrary
import com.example.librarymanagement.repository.LibraryRepository
import com.example.librarymanagement.repository.jpa.mapper.JpaLibraryMapper
import org.springframework.context.annotation.Profile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Repository
@Profile("jpa")
class JpaLibraryRepository(
    private val libraryRepository: LibraryRepositorySpring,
) : LibraryRepository {
    private fun Library.toEntity() = JpaLibraryMapper.toEntity(this)
    private fun JpaLibrary.toDomain() = JpaLibraryMapper.toDomain(this)

    override fun save(library: Library): Mono<Library> =
        Mono.fromCallable { libraryRepository.save(library.toEntity()).toDomain() }
            .subscribeOn(Schedulers.boundedElastic())

    override fun findById(libraryId: String): Mono<Library> =
        Mono.fromCallable { libraryRepository.findByIdOrNull(libraryId.toLong())?.toDomain() }

    override fun findAll(): Flux<Library> = Flux.fromIterable(libraryRepository.findAll().map { it.toDomain() })

    override fun deleteById(libraryId: String): Mono<Unit> =
        Mono.fromCallable { libraryRepository.deleteById(libraryId.toLong()) }
            .subscribeOn(Schedulers.boundedElastic())
            .then(Mono.just(Unit))

    override fun existsById(libraryId: String): Mono<Boolean> =
        Mono.fromCallable { libraryRepository.existsById(libraryId.toLong()) }.subscribeOn(Schedulers.boundedElastic())
}

@Repository
interface LibraryRepositorySpring : JpaRepository<JpaLibrary, Long>
