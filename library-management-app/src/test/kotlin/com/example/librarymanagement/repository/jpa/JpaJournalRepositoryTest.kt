//package com.example.librarymanagement.repository.jpa
//
//import com.example.librarymanagement.data.JournalDataFactory
//import com.example.librarymanagement.data.TestDataFactory
//import io.mockk.every
//import io.mockk.mockk
//import io.mockk.verify
//import org.junit.jupiter.api.Assertions
//import org.junit.jupiter.api.Test
//import java.util.*
//
//class JpaJournalRepositoryTest {
//    private val journalRepositorySpring: JournalRepositorySpring = mockk()
//    private val jpaJournalRepository = JpaJournalRepository(journalRepositorySpring)
//    private val jpaJournal = TestDataFactory.createTestDataRelationsForJpaRepositories().journal
//
//    private val jpaId = JournalDataFactory.JPA_ID
//    private val id = jpaId.toString()
//
//    private val journal = JournalDataFactory.createJournal()
//
//    @Test
//    fun `should save journal`() {
//        //GIVEN
//        val expected = journal
//
//        every { journalRepositorySpring.save(any()) } returns jpaJournal
//
//        //WHEN
//        val actual = jpaJournalRepository.save(journal)
//
//        //THEN
//        Assertions.assertEquals(expected, actual)
//        verify (exactly = 1) {journalRepositorySpring.save(any())}
//    }
//
//    @Test
//    fun findById() {
//        //GIVEN
//        val expected = journal
//
//        every { journalRepositorySpring.findById(any()) } returns Optional.of(jpaJournal)
//
//        //WHEN
//        val actual = jpaJournalRepository.findById(id)
//
//        //THEN
//        Assertions.assertEquals(expected, actual)
//    }
//
//    @Test
//    fun deleteById() {
//        //GIVEN
//        every { journalRepositorySpring.deleteById(any()) } returns Unit
//
//        //WHEN
//        jpaJournalRepository.deleteById(id)
//
//        //THEN
//        verify(exactly = 1) { journalRepositorySpring.deleteById(any()) }
//    }
//
//    @Test
//    fun findAllByUserId() {
//        //GIVEN
//        val expected = listOf(journal)
//
//        every { journalRepositorySpring.findAllByUserId(jpaId) } returns listOf(jpaJournal)
//
//        //WHEN
//        val actual = jpaJournalRepository.findAllByUserId(id)
//
//        //THEN
//        Assertions.assertEquals(expected, actual)
//
//    }
//
//    @Test
//    fun findByBookPresenceIdAndUserIdAndDateOfReturningIsNull() {
//        //GIVEN
//        val expected = jpaJournal
//
//        every { journalRepositorySpring.findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(any(), any()) }
//            .returns(jpaJournal)
//
//        //WHEN
//        val actual = jpaJournalRepository.findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(jpaId, jpaId)
//
//        //THEN
//        Assertions.assertEquals(expected, actual)
//    }
//}
