package com.example.librarymanagement.integration

import com.example.librarymanagement.data.JournalDataFactory
import com.example.librarymanagement.data.ReservationDataFactory
import com.example.librarymanagement.data.UserDataFactory
import com.example.librarymanagement.dto.mapper.JournalMapper
import com.example.librarymanagement.dto.mapper.ReservationMapper
import com.example.librarymanagement.dto.mapper.UserMapper
import com.example.librarymanagement.model.mongo.MongoJournal
import com.example.librarymanagement.model.mongo.MongoReservation
import com.example.librarymanagement.model.mongo.MongoUser
import com.example.librarymanagement.repository.mongo.mapper.MongoJournalMapper
import com.example.librarymanagement.repository.mongo.mapper.MongoReservationMapper
import com.example.librarymanagement.repository.mongo.mapper.MongoUserMapper
import com.fasterxml.jackson.databind.ObjectMapper
import org.bson.types.ObjectId
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class UserControllerIntegrationTest: AbstractIntegrationTest() {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var userMapper: UserMapper

    @Autowired
    private lateinit var reservationMapper: ReservationMapper

    @Autowired
    private lateinit var journalMapper: JournalMapper

    @Autowired
    private lateinit var mongoTemplate: MongoTemplate

    private lateinit var testId: String

    @AfterEach
    fun cleanUp() {
        mongoTemplate.findAndRemove(Query(Criteria.where("id").`is`(testId)), MongoUser::class.java)
        mongoTemplate.findAndRemove(Query(Criteria.where("id").`is`(testId)), MongoReservation::class.java)
        mongoTemplate.findAndRemove(Query(Criteria.where("id").`is`(testId)), MongoJournal::class.java)
    }

    @Test
    fun `should create user`() {
        // Given
        val id = ObjectId()
        testId = id.toString()

        val userDtoRequest = UserDataFactory.createUserRequestDto(testId)
        val userDtoResponce = userMapper.toUserResponseDto(userMapper.toUser(userDtoRequest))
        val content = objectMapper.writeValueAsString(userDtoRequest)
        val expected = objectMapper.writeValueAsString(userDtoResponce)

        // When
        val actual = mockMvc.perform(
            post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
            .andExpect(status().isCreated)
            .andReturn().response.contentAsString

        // Then
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should not create user with invalid data`() {
        // Given
        val id = ObjectId()
        testId = id.toString()

        val userDtoRequest = UserDataFactory.createUserRequestDto(testId).copy(email = "")
        val content = objectMapper.writeValueAsString(userDtoRequest)
        val expected = 400

        // When
        val actual = mockMvc.perform(
            post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
            .andExpect(status().isBadRequest)
            .andReturn().response.status

        // Then
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should update user`() {
        // Given
        val id = ObjectId()
        testId = id.toString()

        val user = UserDataFactory.createMongoUser(id)
        mongoTemplate.save(user)

        val updatedUser = UserDataFactory.createUser(testId).copy(firstName = "Updated")
        val userDtoRequest = UserDataFactory.createUserRequestDto(testId).copy(firstName = "Updated")
        val userDtoResponce = userMapper.toUserResponseDto(updatedUser)

        val content = objectMapper.writeValueAsString(userDtoRequest)
        val expected = objectMapper.writeValueAsString(userDtoResponce)

        // When
        val actual = mockMvc.perform(
            put("$URL/{id}", testId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        // Then
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should get user by id`() {
        // Given
        val id = ObjectId()
        testId = id.toString()

        val user = UserDataFactory.createMongoUser(id)
        mongoTemplate.save(user)

        val expected = objectMapper.writeValueAsString(userMapper.toUserResponseDto(MongoUserMapper.toDomain(user)))

        // When
        val actual = mockMvc.perform(get("${URL}/{id}", testId))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        // Then
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should not get user by invalid id`() {
        // Given
        val id = ObjectId()
        val nonExistingId = ObjectId()
        testId = id.toString()

        val user = UserDataFactory.createMongoUser(id)
        mongoTemplate.save(user)

        val expected = 404

        // When
        val actual = mockMvc.perform(get("${URL}/{id}", nonExistingId))
            .andExpect(status().isNotFound)
            .andReturn().response.status

        // Then
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should get reservation by user id`() {
        // Given
        val id = ObjectId()
        testId = id.toString()

        val user = UserDataFactory.createMongoUser(id)
        val reservation = ReservationDataFactory.createMongoReservation(id).copy(userId = id)
        mongoTemplate.save(user)
        mongoTemplate.save(reservation)

        val expected = objectMapper.writeValueAsString(
            listOf(reservationMapper.toReservationDto(MongoReservationMapper.toDomain(reservation))))

        // When
        val actual = mockMvc.perform(
            get("$URL/{id}/reservations", testId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        // Then
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should get journal by user id`() {
        // Given
        val id = ObjectId()
        testId = id.toString()

        val user = UserDataFactory.createMongoUser(id)
        val journal = JournalDataFactory.createMongoJournal(id).copy(userId = id)

        mongoTemplate.save(user)
        mongoTemplate.save(journal)

        val expected = objectMapper.writeValueAsString(
            listOf(journalMapper.toJournalDto(MongoJournalMapper.toDomain(journal))))

        // When
        val actual = mockMvc.perform(
            get("$URL/{id}/journals", testId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        // Then
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should get user by phone number or email`() {
        // Given
        val id = ObjectId()
        testId = id.toString()

        val user = UserDataFactory.createMongoUser(id)
        mongoTemplate.save(user)

        val expected = objectMapper.writeValueAsString(userMapper.toUserResponseDto(MongoUserMapper.toDomain(user)))

        // When
        val actual = mockMvc.perform(
            get(URL)
                .param("phoneNumber", user.phoneNumber)
                .param("email", user.email)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        // Then
        Assertions.assertEquals(expected, actual)
    }

    companion object {
        const val URL = "/api/v1/user"
    }
}
