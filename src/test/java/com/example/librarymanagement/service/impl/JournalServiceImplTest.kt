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
        //given
        val expected = journal
        every { journalRepository.findById(1) } returns Optional.of(journal)

        //when
        val actual = journalService.getJournalById(1)

        //then
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { journalRepository.findById(1) }
    }

    @Test
    fun shouldCreateJournal() {
        //given
        val expected = journal
        every { journalRepository.save(journal) } returns journal

        //when
        val actual = journalService.createJournal(journal)

        //then
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { journalRepository.save(journal) }
    }

    @Test
    fun shouldUpdateJournal() {
        //given
        val expected = journal.copy(dateOfReturning = LocalDate.now())
        every { journalRepository.findById(1) } returns Optional.of(journal)
        every { journalRepository.save(expected) } returns expected

        //when
        val actual = journalService.updateJournal(1, expected)

        //then
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { journalRepository.findById(1) }
        verify(exactly = 1) { journalRepository.save(expected) }
    }

    @Test
    fun shouldDeleteJournal() {
        //given
        every { journalRepository.findById(1) } returns Optional.of(journal)
        every { journalRepository.deleteById(1) } returns Unit

        //when
        journalService.deleteJournal(1)

        //then
        verify(exactly = 1) { journalRepository.deleteById(1) }
    }

    @Test
    fun shouldFindByBookPresenceIdAndUserIdAndDateOfReturningIsNull(){
        //given
        val expected = journal
        every { journalRepository.findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(1, 1) } returns journal

        //when
        val actual = journalService.findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(1, 1)

        //then
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { journalRepository.findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(1, 1) }
    }

    @Test
    fun shouldGetJournalByBookPresenceIdAndUserId(){
        //given
        val expected = listOf(journal)
        every { journalRepository.findByBookPresenceIdAndUserId(1, 1) } returns listOf(journal)

        //when
        val actual = journalService.getJournalByBookPresenceIdAndUserId(1, 1)

        //then
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { journalRepository.findByBookPresenceIdAndUserId(1, 1) }
    }

    @Test
    fun shouldGetJournalByUserId(){
        //given
        val expected = listOf(journal)
        every { journalRepository.findAllByUserId(1) } returns listOf(journal)

        //when
        val actual = journalService.getJournalByUserId(1)

        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { journalRepository.findAllByUserId(1) }
    }
}
