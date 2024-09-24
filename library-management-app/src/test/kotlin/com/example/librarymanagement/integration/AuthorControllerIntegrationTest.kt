package com.example.librarymanagement.integration

import com.example.librarymanagement.data.AuthorDataFactory
import com.example.librarymanagement.dto.mapper.AuthorMapper
import com.example.librarymanagement.model.mongo.MongoAuthor
import com.example.librarymanagement.repository.mongo.mapper.MongoAuthorMapper
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

class AuthorControllerIntegrationTest : AbstractIntegrationTest() {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var authorMapper: AuthorMapper

    @Autowired
    private lateinit var mongoTemplate: MongoTemplate

    private lateinit var testId: String

    @AfterEach
    fun cleanUp() {
        mongoTemplate.findAndRemove(Query(Criteria.where("id").`is`(testId)), MongoAuthor::class.java)
    }

    @Test
    fun `should create author`() {
        // Given
        val id = ObjectId()
        testId = id.toString()

        val author = AuthorDataFactory.createMongoAuthor(id)
        val authorDto = authorMapper.toAuthorDto(MongoAuthorMapper.toDomain(author))
        val content = objectMapper.writeValueAsString(authorDto)
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
    fun `should not create author with invalid data`() {
        // Given
        val id = ObjectId()
        testId = id.toString()

        val expected = 400
        val author = AuthorDataFactory.createMongoAuthor(id)
        val authorDto = authorMapper.toAuthorDto(MongoAuthorMapper.toDomain(author))
        val content = objectMapper.writeValueAsString(authorDto.copy(lastName = ""))

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
    fun `should not update author with invalid data`() {
        // Given
        val id = ObjectId()
        testId = id.toString()

        val expected = 400
        val author = AuthorDataFactory.createMongoAuthor(id)
        val authorDto = authorMapper.toAuthorDto(MongoAuthorMapper.toDomain(author))
        val content = objectMapper.writeValueAsString(authorDto.copy(firstName = ""))

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
    fun `should get all authors`() {
        // Given
        val id = ObjectId()
        testId = id.toString()

        val authorMongo = AuthorDataFactory.createMongoAuthor(id)
        val author = MongoAuthorMapper.toDomain(authorMongo)
        val authorDto = authorMapper.toAuthorDto(author)

        val expected = objectMapper.writeValueAsString(listOf(authorDto))
        mongoTemplate.save(author)

        // When
        val actual = mockMvc.perform(get(URL))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        // Then
        Assertions.assertEquals(expected, actual)
    }
    @Test
    fun `should get author by id`() {
        // Given
        val id = ObjectId()
        testId = id.toString()

        val authorMongo = AuthorDataFactory.createMongoAuthor(id)
        val author = MongoAuthorMapper.toDomain(authorMongo)
        val authorDto = authorMapper.toAuthorDto(author)

        val expected = objectMapper.writeValueAsString(authorDto)
        mongoTemplate.save(author)

        // When
        val actual = mockMvc.perform(get("$URL/{id}", id))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        // Then
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should not get author by id`() {
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
        const val URL = "/api/v1/author"
    }
}
