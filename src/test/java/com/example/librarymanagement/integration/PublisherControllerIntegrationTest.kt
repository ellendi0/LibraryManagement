package com.example.librarymanagement.integration

import com.example.librarymanagement.data.PublisherDataFactory
import com.example.librarymanagement.dto.mapper.PublisherMapper
import com.example.librarymanagement.model.mongo.MongoPublisher
import com.example.librarymanagement.repository.mongo.mapper.MongoPublisherMapper
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class PublisherControllerIntegrationTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var publisherMapper: PublisherMapper

    @Autowired
    private lateinit var mongoTemplate: MongoTemplate

    private lateinit var testId: String

    @AfterEach
    fun cleanUp() {
        mongoTemplate.findAndRemove(Query(Criteria.where("id").`is`(testId)), MongoPublisher::class.java)
    }

    @Test
    fun `should create publisher`() {
        // Given
        val id = ObjectId()
        testId = id.toString()

        val publisher = PublisherDataFactory.createMongoPublisher(id)
        val publisherDto = publisherMapper.toPublisherDto(MongoPublisherMapper.toDomain(publisher))
        val content = objectMapper.writeValueAsString(publisherDto)
        val expected = content

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
    fun `should not create publisher with invalid data`() {
        // Given
        val id = ObjectId()
        testId = id.toString()

        val expected = 400
        val publisher = PublisherDataFactory.createMongoPublisher(id)
        val publisherDto = publisherMapper.toPublisherDto(MongoPublisherMapper.toDomain(publisher))
        val content = objectMapper.writeValueAsString(publisherDto.copy(name = ""))

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
    fun `should not update publisher with invalid data`() {
        // Given
        val id = ObjectId()
        testId = id.toString()

        val expected = 400
        val publisher = PublisherDataFactory.createMongoPublisher(id)
        val publisherDto = publisherMapper.toPublisherDto(MongoPublisherMapper.toDomain(publisher))
        val content = objectMapper.writeValueAsString(publisherDto.copy(name = ""))

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
    fun `should get all publishers`() {
        // Given
        val id = ObjectId()
        testId = id.toString()

        val publisherMongo = PublisherDataFactory.createMongoPublisher(id)
        val publisher = MongoPublisherMapper.toDomain(publisherMongo)
        val publisherDto = publisherMapper.toPublisherDto(publisher)

        val expected = objectMapper.writeValueAsString(listOf(publisherDto))
        mongoTemplate.save(publisher)

        // When
        val actual = mockMvc.perform(get(URL))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        // Then
        Assertions.assertEquals(expected, actual)
    }
    @Test
    fun `should get publisher by id`() {
        // Given
        val id = ObjectId()
        testId = id.toString()

        val publisherMongo = PublisherDataFactory.createMongoPublisher(id)
        val publisher = MongoPublisherMapper.toDomain(publisherMongo)
        val publisherDto = publisherMapper.toPublisherDto(publisher)

        val expected = objectMapper.writeValueAsString(publisherDto)
        mongoTemplate.save(publisher)

        // When
        val actual = mockMvc.perform(get("$URL/{id}", id))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        // Then
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should not get publisher by id`() {
        // Given
        val expected = 404

        val id = ObjectId()
        testId = id.toString()
        val nonExistingId = ObjectId()

        // When
        val actual = mockMvc.perform(get("$URL/{id}", nonExistingId))
            .andExpect(status().isNotFound)
            .andReturn().response.status

        // Then
        Assertions.assertEquals(expected, actual)
    }

    companion object {
        const val URL = "/api/v1/publisher"
    }
}
