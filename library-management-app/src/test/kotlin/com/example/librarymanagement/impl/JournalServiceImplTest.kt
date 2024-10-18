package com.example.librarymanagement.service.impl

import com.example.librarymanagement.data.JournalDataFactory
import com.example.librarymanagement.repository.JournalRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class JournalServiceImplTest {
    private val journalRepository: JournalRepository = mockk()
    private val journalService = JournalServiceImpl(journalRepository)

    private val id = JournalDataFactory.ID

    private val journal = JournalDataFactory.createJournal(id)

    @Test
    fun `should get journal by id`() {
        // GIVEN
        val expected = journal

        every { journalRepository.findById(id) } returns Mono.just(journal)

        // WHEN
        val result = StepVerifier.create(journalService.getJournalById(id))

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
        }.verifyComplete()
    }

    @Test
    fun `should create journal`() {
        // GIVEN
        val expected = journal

        every { journalRepository.saveOrUpdate(journal) } returns Mono.just(journal)

        // WHEN
        val result = StepVerifier.create(journalService.save(journal))

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
            verify(exactly = 1) { journalRepository.saveOrUpdate(journal) }
        }.verifyComplete()
    }

    @Test
    fun `should delete journal by id journal`() {
        // GIVEN
        val expected = Unit

        every { journalRepository.findById(id) } returns Mono.just(journal)
        every { journalRepository.deleteById(id) } returns Mono.just(Unit)

        // WHEN
        val result = StepVerifier.create(journalService.deleteJournalById(id))

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
            verify(exactly = 1) { journalRepository.deleteById(id) }
        }.verifyComplete()
    }

    @Test
    fun `should find by book presence id and user id and date of returning is null`() {
        // GIVEN
        val expected = journal

        every { journalRepository.findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(id, id) }
            .returns(Mono.just(journal))

        // WHEN
        val result = StepVerifier.create(journalService.getByBookPresenceIdAndUserIdAndDateOfReturningIsNull(id, id))

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
            verify(exactly = 1) { journalRepository.findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(id, id) }
        }.verifyComplete()
    }

    @Test
    fun `should get journal by user id`() {
        // GIVEN
        val expected = journal

        every { journalRepository.findAllByUserId(id) } returns Flux.just(journal)

        // WHEN
        val result = StepVerifier.create(journalService.getJournalByUserId(id))

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
            verify(exactly = 1) { journalRepository.findAllByUserId(id) }
        }.verifyComplete()
    }
}
