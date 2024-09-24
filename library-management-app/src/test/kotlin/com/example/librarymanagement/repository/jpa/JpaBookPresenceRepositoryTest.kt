package com.example.librarymanagement.repository.jpa

import com.example.librarymanagement.data.BookDataFactory
import com.example.librarymanagement.data.BookPresenceDataFactory
import com.example.librarymanagement.data.TestDataFactory
import com.example.librarymanagement.data.UserDataFactory
import com.example.librarymanagement.model.enums.Availability
import com.example.librarymanagement.model.jpa.JpaJournal
import com.example.librarymanagement.repository.jpa.JpaAuthorRepositoryTest.Companion.ID
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.*

class JpaBookPresenceRepositoryTest {
    private val bookPresenceRepositorySpring: BookPresenceRepositorySpring = mockk()
    private val journalRepository: JpaJournalRepository = mockk()
    private val libraryRepository: JpaLibraryRepository = mockk()
    private val bookRepository: JpaBookRepository = mockk()
    private val userRepository: JpaUserRepository = mockk()


    private val jpaBookPresenceRepository = JpaBookPresenceRepository(
        bookPresenceRepositorySpring,
        libraryRepository,
        userRepository,
        bookRepository,
        journalRepository
    )

    private val id = BookDataFactory.JPA_ID.toString()

    private val testDataFactory = TestDataFactory.createTestDataRelationsForJpaRepositories()
    private val jpaBookPresence = testDataFactory.bookPresence
    private val bookPresence = BookPresenceDataFactory.createBookPresence()
    private val user = UserDataFactory.createUser(id)

    @Test
    fun `should save book presence`() {
        //GIVEN
        val expected = bookPresence

        every { userRepository.findById(id) } returns Mono.just(user)
        every { bookPresenceRepositorySpring.findById(id.toLong()) } returns Optional.of(jpaBookPresence)
        every { bookPresenceRepositorySpring.save(any()) } returns jpaBookPresence

        //WHEN
        val result = StepVerifier.create(jpaBookPresenceRepository.saveOrUpdate(bookPresence))

        //THEN
        result
            .assertNext { actual ->
                Assertions.assertEquals(expected, actual)
                verify(exactly = 1) { bookPresenceRepositorySpring.save(any())}
            }
            .verifyComplete()
    }
//
//    @Test
//    fun shouldAddBookToUser() {
//        // GIVEN
//        val jpaJournal = testDataFactory.journal
//        val expected = bookPresence.copy(availability = Availability.UNAVAILABLE)
//        val user = UserDataFactory.createUser()
//
//        every {
//            bookPresenceRepositorySpring.findAllByLibraryIdAndBookIdAndAvailability(
//                any(),
//                any(),
//                Availability.AVAILABLE
//            )
//        } returns listOf(jpaBookPresence)
//
//        every { journalRepository.save(any<JpaJournal>()) } returns jpaJournal
//        every { bookPresenceRepositorySpring.save(any()) } returns jpaBookPresence
//
//        // WHEN
//        val actual = jpaBookPresenceRepository.addBookToUser(user, id, id)
//
//        // THEN
//        Assertions.assertEquals(expected, actual)
//        verify(exactly = 1) { journalRepository.save(any<JpaJournal>()) }
//        verify(exactly = 1) { bookPresenceRepositorySpring.save(any()) }
//    }
//
//    @Test
//    fun shouldFindById() {
//        //GIVEN
//        val expected = bookPresence
//
//        every { bookPresenceRepositorySpring.findById(any()) } returns Optional.of(jpaBookPresence)
//
//        //WHEN
//        val actual = jpaBookPresenceRepository.findById(id)
//
//        //THEN
//        Assertions.assertEquals(expected, actual)
//    }
//
//    @Test
//    fun shouldFindAllByBookId() {
//        //GIVEN
//        val expected = listOf(bookPresence)
//
//        every { bookPresenceRepositorySpring.findAllByBookId(any()) } returns listOf(jpaBookPresence)
//
//        //WHEN
//        val actual = jpaBookPresenceRepository.findAllByBookId(id)
//
//        //THEN
//        Assertions.assertEquals(expected, actual)
//    }
//
//    @Test
//    fun shouldFindAllByLibraryId() {
//        //GIVEN
//        val expected = listOf(bookPresence)
//
//        every { bookPresenceRepositorySpring.findAllByLibraryId(any()) } returns listOf(jpaBookPresence)
//
//        //WHEN
//        val actual = jpaBookPresenceRepository.findAllByLibraryId(id)
//
//        //THEN
//        Assertions.assertEquals(expected, actual)
//    }
//
//    @Test
//    fun shouldFindAllByUserId() {
//        //GIVEN
//        val expected = listOf(bookPresence)
//
//        every { bookPresenceRepositorySpring.findAllByUserId(any()) } returns listOf(jpaBookPresence)
//
//        //WHEN
//        val actual = jpaBookPresenceRepository.findAllByUserId(id)
//
//        //THEN
//        Assertions.assertEquals(expected, actual)
//    }
//
//    @Test
//    fun shouldFindAllByLibraryIdAndBookId() {
//        //GIVEN
//        val expected = listOf(bookPresence)
//
//        every { bookPresenceRepositorySpring.findAllByLibraryIdAndBookId(any(), any()) } returns listOf(jpaBookPresence)
//
//        //WHEN
//        val actual = jpaBookPresenceRepository.findAllByLibraryIdAndBookId(id, id)
//
//        //THEN
//        Assertions.assertEquals(expected, actual)
//    }
//
//    @Test
//    fun shouldFindAllByLibraryIdAndBookIdAndAvailability() {
//        //GIVEN
//        val expected = listOf(bookPresence)
//
//        every {
//            bookPresenceRepositorySpring
//                .findAllByLibraryIdAndBookIdAndAvailability(any(), any(), Availability.AVAILABLE)
//        }
//            .returns(listOf(jpaBookPresence))
//
//        //WHEN
//        val actual =
//            jpaBookPresenceRepository.findAllByLibraryIdAndBookIdAndAvailability(id, id, Availability.AVAILABLE)
//
//        //THEN
//        Assertions.assertEquals(expected, actual)
//    }
//
//    @Test
//    fun shouldDeleteById() {
//        //GIVEN
//        every { bookPresenceRepositorySpring.deleteById(any()) } returns Unit
//
//        //WHEN
//        jpaBookPresenceRepository.deleteById(id)
//
//        //THEN
//        verify(exactly = 1) { bookPresenceRepositorySpring.deleteById(any()) }
//    }
}
