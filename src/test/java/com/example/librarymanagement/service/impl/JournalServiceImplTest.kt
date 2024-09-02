package com.example.librarymanagement.service.impl

import com.example.librarymanagement.data.JournalDataFactory
import com.example.librarymanagement.repository.JournalRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDate

class JournalServiceImplTest {
    private val journalRepository: JournalRepository = mockk()
    private val journalService = JournalServiceImpl(journalRepository)

    private val id = "1"

    private val journal = JournalDataFactory.createJournal(id)

    @Test
    fun shouldGetJournalById() {
        //given
        val expected = journal

        every { journalRepository.findById(id) } returns journal

        //when
        val actual = journalService.getJournalById(id)

        //then
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { journalRepository.findById(id) }
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
        every { journalRepository.findById(id) } returns journal
        every { journalRepository.save(expected) } returns expected

        //when
        val actual = journalService.updateJournal(id, expected)

        //then
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { journalRepository.findById(id) }
        verify(exactly = 1) { journalRepository.save(expected) }
    }

    @Test
    fun shouldDeleteJournalByIdJournal() {
        //given
        every { journalRepository.findById(id) } returns journal
        every { journalRepository.deleteById(id) } returns Unit

        //when
        journalService.deleteJournalById(id)

        //then
        verify(exactly = 1) { journalRepository.deleteById(id) }
    }

    @Test
    fun shouldFindByBookPresenceIdAndUserIdAndDateOfReturningIsNull() {
        //given
        val expected = journal
        every { journalRepository.findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(id, id) } returns journal

        //when
        val actual = journalService.findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(id, id)

        //then
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { journalRepository.findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(id, id) }
    }

    @Test
    fun shouldGetJournalByUserId() {
        //given
        val expected = listOf(journal)
        every { journalRepository.findAllByUserId(id) } returns listOf(journal)

        //when
        val actual = journalService.getJournalByUserId(id)

        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { journalRepository.findAllByUserId(id) }
    }
}
