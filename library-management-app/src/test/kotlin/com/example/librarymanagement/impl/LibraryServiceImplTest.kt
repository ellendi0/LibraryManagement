package com.example.librarymanagement.service.impl

import com.example.librarymanagement.data.LibraryDataFactory
import com.example.librarymanagement.repository.LibraryRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class LibraryServiceImplTest {
    private val libraryRepository: LibraryRepository = mockk()
    private val libraryService = LibraryServiceImpl(
        libraryRepository
    )
    private val id = LibraryDataFactory.ID

    private val library = LibraryDataFactory.createLibrary(id)

    @Test
    fun `should create library`() {
        // GIVEN
        val expected = library

        every { libraryRepository.save(library) } returns Mono.just(library)

        // WHEN
        val result = StepVerifier.create(libraryService.createLibrary(library))

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
            verify(exactly = 1) { libraryRepository.save(library) }
        }.verifyComplete()
    }

    @Test
    fun `should update library`() {
        // GIVEN
        val expected = library

        every { libraryRepository.findById(id) } returns Mono.just(library)
        every { libraryRepository.save(library) } returns Mono.just(library)

        // WHEN
        val result = StepVerifier.create(libraryService.createLibrary(library))

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
            verify(exactly = 1) { libraryRepository.save(library) }
        }.verifyComplete()
    }

    @Test
    fun `should delete library by id`() {
        // GIVEN
        val expected = Unit

        every { libraryRepository.deleteById(id) } returns Mono.just(Unit)

        // WHEN
        val result = StepVerifier.create(libraryService.deleteLibraryById(id))

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
            verify(exactly = 1) { libraryRepository.deleteById(id) }
        }.verifyComplete()
    }

    @Test
    fun `should get library by id`() {
        // GIVEN
        val expected = library

        every { libraryRepository.findById(id) } returns Mono.just(library)

        // WHEN
        val result = StepVerifier.create(libraryService.getLibraryById(id))

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
        }.verifyComplete()
    }

    @Test
    fun `should get all libraries`() {
        // GIVEN
        val expected = library

        every { libraryRepository.findAll() } returns Flux.just(library)

        // WHEN
        val result = StepVerifier.create(libraryService.getAllLibraries())

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
        }.verifyComplete()
    }
}
