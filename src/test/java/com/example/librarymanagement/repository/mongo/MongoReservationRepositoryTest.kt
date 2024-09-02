package com.example.librarymanagement.repository.mongo

import com.example.librarymanagement.data.ReservationDataFactory
import com.example.librarymanagement.model.mongo.MongoReservation
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.data.mongodb.core.MongoTemplate

class MongoReservationRepositoryTest {
    private val mongoTemplate: MongoTemplate = mockk()

    private val mongoReservationRepository = MongoReservationRepository(mongoTemplate)

    private val mongoId = ReservationDataFactory.MONGO_ID
    private val id = mongoId.toString()

    private val reservation = ReservationDataFactory.createReservation(id)
    private val mongoReservation = ReservationDataFactory.createMongoReservation()

    @Test
    fun `should save`() {
        //GIVEN
        val expected = reservation

        every { mongoTemplate.save(mongoReservation) } returns mongoReservation

        //WHEN
        val actual = mongoReservationRepository.save(reservation)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { mongoTemplate.save(mongoReservation) }
    }

    @Test
    fun `should find by id`() {
        //GIVEN
        val expected = reservation

        every { mongoTemplate.findById(any(), MongoReservation::class.java) } returns mongoReservation

        //WHEN
        val actual = mongoReservationRepository.findById(id)

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should delete by id`() {
        //GIVEN
        every { mongoTemplate.findAndRemove(any(), MongoReservation::class.java) } returns mongoReservation

        //WHEN
        mongoReservationRepository.deleteById(id)

        //THEN
        verify { mongoTemplate.findAndRemove(any(), MongoReservation::class.java) }
    }

    @Test
    fun `should find all bookId and userId`() {
        //GIVEN
        val expected = listOf(reservation)

        every { mongoTemplate.find(any(), MongoReservation::class.java) } returns listOf(mongoReservation)

        //WHEN
        val actual = mongoReservationRepository.findAllByBookIdAndUserId(id, id)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) {
            mongoTemplate.find(
                match {
                    it.queryObject[MongoReservation::bookId.name] == mongoId &&
                            it.queryObject[MongoReservation::userId.name] == mongoId
                }, MongoReservation::class.java
            )
        }
    }

    @Test
    fun `should find all by libraryId`() {
        //GIVEN
        val expected = listOf(reservation)

        every { mongoTemplate.find(any(), MongoReservation::class.java) } returns listOf(mongoReservation)

        //WHEN
        val actual = mongoReservationRepository.findAllByLibraryId(id)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) {
            mongoTemplate.find(
                match {
                    it.queryObject[MongoReservation::libraryId.name] == mongoId
                }, MongoReservation::class.java
            )
        }
    }

    @Test
    fun `should find all by userId`() {
        //GIVEN
        val expected = listOf(reservation)

        every { mongoTemplate.find(any(), MongoReservation::class.java) } returns listOf(mongoReservation)

        //WHEN
        val actual = mongoReservationRepository.findAllByUserId(id)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) {
            mongoTemplate.find(
                match {
                    it.queryObject[MongoReservation::userId.name] == mongoId
                }, MongoReservation::class.java
            )
        }
    }

    @Test
    fun `should find all by bookId`() {
        //GIVEN
        val expected = listOf(reservation)

        every { mongoTemplate.find(any(), MongoReservation::class.java) } returns listOf(mongoReservation)

        //WHEN
        val actual = mongoReservationRepository.findAllByBookId(id)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) {
            mongoTemplate.find(
                match {
                    it.queryObject[MongoReservation::bookId.name] == mongoId
                }, MongoReservation::class.java
            )
        }
    }

    @Test
    fun `should find first by bookId when libraryId is null`() {
        // GIVEN
        val expected = reservation

        every { mongoTemplate.findOne(any(), MongoReservation::class.java) } returns mongoReservation

        // WHEN
        val actual = mongoReservationRepository.findFirstByBookIdAndLibraryIdOrLibraryIsNull(id, null)

        // THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) {
            mongoTemplate.findOne(
                match {
                    it.queryObject[MongoReservation::bookId.name] == mongoId &&
                            it.queryObject[MongoReservation::libraryId.name] == null &&
                            it.queryObject["\$or"] != null
                }, MongoReservation::class.java
            )
        }
    }

    @Test
    fun `should find first by bookId, libraryId is not null`() {
        //GIVEN
        val expected = reservation

        every { mongoTemplate.findOne(any(), MongoReservation::class.java) } returns mongoReservation

        //WHEN
        val actual = mongoReservationRepository.findFirstByBookIdAndLibraryIdOrLibraryIsNull(id, id)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) {
            mongoTemplate.findOne(
                match {
                    it.queryObject[MongoReservation::bookId.name] == mongoId &&
                            it.queryObject[MongoReservation::libraryId.name] == mongoId
                }, MongoReservation::class.java
            )
        }
    }

    @Test
    fun `should return true if exists by bookId and userId`() {
        //GIVEN
        val expected = true

        every { mongoTemplate.exists(any(), MongoReservation::class.java) } returns true

        //WHEN
        val actual = mongoReservationRepository.existsByBookIdAndUserId(id, id)

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should find all by bookId and libraryId`() {
        //GIVEN
        val expected = listOf(reservation)

        every { mongoTemplate.find(any(), MongoReservation::class.java) } returns listOf(mongoReservation)

        //WHEN
        val actual = mongoReservationRepository.findAllByBookIdAndLibraryId(id, id)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) {
            mongoTemplate.find(
                match {
                    it.queryObject[MongoReservation::bookId.name] == mongoId &&
                            it.queryObject[MongoReservation::libraryId.name] == mongoId
                }, MongoReservation::class.java
            )
        }
    }
}
