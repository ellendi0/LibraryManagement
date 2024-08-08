package com.example.librarymanagement.service.impl

import com.example.librarymanagement.data.TestDataFactory
import com.example.librarymanagement.repository.JournalRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.*

class JournalServiceImplTest {
    private val journalRepository: JournalRepository = mockk()
    private val journalService = JournalServiceImpl(journalRepository)

    private val journal = TestDataFactory.createTestDataRelForServices().journal

    @Test
    fun shouldGetJournalById() {
        every { journalRepository.findById(1) } returns Optional.of(journal)

        Assertions.assertEquals(journal, journalService.getJournalById(1))
        verify(exactly = 1) { journalRepository.findById(1) }
    }

    @Test
    fun shouldCreateJournal() {
        every { journalRepository.save(journal) } returns journal

        Assertions.assertEquals(journal, journalService.createJournal(journal))
        verify(exactly = 1) { journalRepository.save(journal) }
    }

    @Test
    fun shouldUpdateJournal() {
        val updatedJournal = journal.copy(dateOfReturning = LocalDate.now())
        every { journalRepository.findById(1) } returns Optional.of(journal)
        every { journalRepository.save(updatedJournal) } returns updatedJournal

        Assertions.assertEquals(updatedJournal, journalService.updateJournal(1, updatedJournal))
        verify(exactly = 1) { journalRepository.findById(1) }
        verify(exactly = 1) { journalRepository.save(updatedJournal) }
    }

    @Test
    fun shouldDeleteJournal() {
        every { journalRepository.findById(1) } returns Optional.of(journal)
        every { journalRepository.deleteById(1) } returns Unit

        journalService.deleteJournal(1)
        verify(exactly = 1) { journalRepository.deleteById(1) }
    }

    @Test
    fun shouldFindByBookPresenceIdAndUserIdAndDateOfReturningIsNull(){
        every { journalRepository.findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(1, 1) } returns journal

        Assertions.assertEquals(journal, journalService.findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(1, 1))
        verify(exactly = 1) { journalRepository.findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(1, 1) }
    }

    @Test
    fun shouldGetJournalByBookPresenceIdAndUserId(){
        every { journalRepository.findByBookPresenceIdAndUserId(1, 1) } returns listOf(journal)

        Assertions.assertEquals(listOf(journal), journalService.getJournalByBookPresenceIdAndUserId(1, 1))
        verify(exactly = 1) { journalRepository.findByBookPresenceIdAndUserId(1, 1) }
    }

    @Test
    fun shouldGetJournalByUserId(){
        every { journalRepository.findAllByUserId(1) } returns listOf(journal)

        Assertions.assertEquals(listOf(journal), journalService.getJournalByUserId(1))
        verify(exactly = 1) { journalRepository.findAllByUserId(1) }
    }
}
