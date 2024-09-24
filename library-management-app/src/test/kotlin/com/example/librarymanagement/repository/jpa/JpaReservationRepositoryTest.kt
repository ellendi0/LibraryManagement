//package com.example.librarymanagement.repository.jpa
//
//import com.example.librarymanagement.data.BookDataFactory
//import com.example.librarymanagement.data.LibraryDataFactory
//import com.example.librarymanagement.data.ReservationDataFactory
//import com.example.librarymanagement.data.TestDataFactory
//import com.example.librarymanagement.data.UserDataFactory
//import io.mockk.every
//import io.mockk.mockk
//import io.mockk.verify
//import org.junit.jupiter.api.Assertions
//import org.junit.jupiter.api.Test
//import java.util.*
//
//class JpaReservationRepositoryTest {
//    private val reservationRepositorySpring: ReservationRepositorySpring = mockk()
//    private val userRepository: JpaUserRepository = mockk()
//    private val bookRepository: JpaBookRepository = mockk()
//    private val libraryRepository: JpaLibraryRepository = mockk()
//
//    private val jpaReservationRepository = JpaReservationRepository(
//        reservationRepositorySpring,
//        userRepository,
//        bookRepository,
//    )
//
//    private val jpaId = ReservationDataFactory.JPA_ID
//    private val id = jpaId.toString()
//
//    private val reservation = ReservationDataFactory.createReservation(id)
//    private val jpaReservation = TestDataFactory.createTestDataRelationsForJpaRepositories().reservation
//
//    @Test
//    fun `should save if library is not null`() {
//        //GIVEN
//        val expected = reservation
//        val user = UserDataFactory.createUser(id)
//        val book = BookDataFactory.createBook(id)
//        val library = LibraryDataFactory.createLibrary(id)
//
//        every { userRepository.findById(any()) } returns user
//        every { bookRepository.findById(any()) } returns book
//        every { libraryRepository.findById(any()) } returns library
//        every { reservationRepositorySpring.save(any()) } returns jpaReservation
//
//        //WHEN
//        val actual = jpaReservationRepository.save(reservation)
//
//        //THEN
//        Assertions.assertEquals(expected, actual)
//        verify(exactly = 1) { reservationRepositorySpring.save(any()) }
//    }
//
//    @Test
//    fun `should find by id`() {
//        //GIVEN
//        val expected = reservation
//
//        every { reservationRepositorySpring.findById(any()) } returns Optional.of(jpaReservation)
//
//        //WHEN
//        val actual = jpaReservationRepository.findById(id)
//
//        //THEN
//        Assertions.assertEquals(expected, actual)
//    }
//
//    @Test
//    fun `should delete by id`() {
//        //GIVEN
//        every { reservationRepositorySpring.deleteById(any()) } returns Unit
//
//        //WHEN
//        jpaReservationRepository.deleteById(id)
//
//        //THEN
//        verify { reservationRepositorySpring.deleteById(any()) }
//    }
//
//    @Test
//    fun `should find all bookId and userId`() {
//        //GIVEN
//        val expected = listOf(reservation)
//
//        every { reservationRepositorySpring.findAllByBookId(any()) } returns listOf(jpaReservation)
//
//        //WHEN
//        val actual = jpaReservationRepository.findAllByBookId(id)
//
//        //THEN
//        Assertions.assertEquals(expected, actual)
//    }
//
//    @Test
//    fun `should find all by libraryId`() {
//        //GIVEN
//        val expected = listOf(reservation)
//
//        every { reservationRepositorySpring.findAllByLibraryId(any()) } returns listOf(jpaReservation)
//
//        //WHEN
//        val actual = jpaReservationRepository.findAllByLibraryId(id)
//
//        //THEN
//        Assertions.assertEquals(expected, actual)
//    }
//
//    @Test
//    fun `should find all by userId`() {
//        //GIVEN
//        val expected = listOf(reservation)
//
//        every { reservationRepositorySpring.findAllByUserId(any()) } returns listOf(jpaReservation)
//
//        //WHEN
//        val actual = jpaReservationRepository.findAllByUserId(id)
//
//        //THEN
//        Assertions.assertEquals(expected, actual)
//    }
//
//    @Test
//    fun `should find all by bookId`() {
//        //GIVEN
//        val expected = listOf(reservation)
//
//        every { reservationRepositorySpring.findAllByBookId(any()) } returns listOf(jpaReservation)
//
//        //WHEN
//        val actual = jpaReservationRepository.findAllByBookId(id)
//
//        //THEN
//        Assertions.assertEquals(expected, actual)
//    }
//
//    @Test
//    fun `should find first by bookId, libraryId is null`() {
//        //GIVEN
//        val expected = reservation
//
//        every { reservationRepositorySpring.findFirstByBookIdAndLibraryIdOrLibraryIsNull(any(), null) }
//            .returns(jpaReservation)
//
//        //WHEN
//        val actual = jpaReservationRepository.findFirstByBookIdAndLibraryId(id, null)
//
//        //THEN
//        Assertions.assertEquals(expected, actual)
//    }
//
//    @Test
//    fun `should find first by bookId, libraryId is not null`() {
//        //GIVEN
//        val expected = reservation
//
//        every { reservationRepositorySpring.findFirstByBookIdAndLibraryIdOrLibraryIsNull(any(), any()) }
//            .returns(jpaReservation)
//
//        //WHEN
//        val actual = jpaReservationRepository.findFirstByBookIdAndLibraryId(id, id)
//
//        //THEN
//        Assertions.assertEquals(expected, actual)
//    }
//
//    @Test
//    fun `should return true if exists by bookId and userId`() {
//        //GIVEN
//        val expected = true
//
//        every { reservationRepositorySpring.existsByBookIdAndUserId(any(), any()) } returns true
//
//        //WHEN
//        val actual = jpaReservationRepository.existsByBookIdAndUserId(id, id)
//
//        //THEN
//        Assertions.assertEquals(expected, actual)
//    }
//
//    @Test
//    fun `should find all by bookId and libraryId`() {
//        //GIVEN
//        val expected = listOf(reservation)
//
//        every { reservationRepositorySpring.findAllByBookIdAndLibraryId(any(), any()) } returns listOf(jpaReservation)
//
//        //WHEN
//        val actual = jpaReservationRepository.findAllByBookIdAndLibraryId(id, id)
//
//        //THEN
//        Assertions.assertEquals(expected, actual)
//    }
//}
