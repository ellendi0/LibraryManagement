package com.example.librarymanagement.integration

import com.example.librarymanagement.data.LibraryDataFactory
import com.example.librarymanagement.dto.mapper.LibraryMapper
import com.example.librarymanagement.model.mongo.MongoLibrary
import com.example.librarymanagement.repository.mongo.mapper.MongoLibraryMapper
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class LibraryControllerIntegrationTest: AbstractIntegrationTest() {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var libraryMapper: LibraryMapper

    @Autowired
    private lateinit var mongoTemplate: MongoTemplate

    private lateinit var testId: String

    @AfterEach
    fun cleanUp() {
        mongoTemplate.findAndRemove(Query(Criteria.where("id").`is`(testId)), MongoLibrary::class.java)
    }

    @Test
    fun `should create library`() {
        // Given
        val id = ObjectId()
        testId = id.toString()

        val library = LibraryDataFactory.createMongoLibrary(id)
        val libraryDto = libraryMapper.toLibraryDto(MongoLibraryMapper.toDomain(library))
        val content = objectMapper.writeValueAsString(libraryDto)
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
    fun `should not create library with invalid data`() {
        // Given
        val id = ObjectId()
        testId = id.toString()

        val library = LibraryDataFactory.createMongoLibrary(id)
        val libraryDto = libraryMapper.toLibraryDto(MongoLibraryMapper.toDomain(library))
        val content = objectMapper.writeValueAsString(libraryDto.copy(name = ""))
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
    fun `should update library`() {
        // Given
        val id = ObjectId()
        testId = id.toString()

        val library = LibraryDataFactory.createMongoLibrary(id)
        mongoTemplate.save(library)

        val updatedLibrary = LibraryDataFactory.createMongoLibrary(id).copy(name = "Updated")
        val libraryDto = libraryMapper.toLibraryDto(MongoLibraryMapper.toDomain(updatedLibrary))
        val content = objectMapper.writeValueAsString(libraryDto)
        val expected = content

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
    fun `should not update library with invalid data`() {
        // Given
        val id = ObjectId()
        testId = id.toString()

        val library = LibraryDataFactory.createMongoLibrary(id)
        mongoTemplate.save(library)

        val updatedLibrary = LibraryDataFactory.createMongoLibrary(id).copy(name = "")
        val libraryDto = libraryMapper.toLibraryDto(MongoLibraryMapper.toDomain(updatedLibrary))
        val content = objectMapper.writeValueAsString(libraryDto)
        val expected = 400

        // When
        val actual = mockMvc.perform(
            put("$URL/{id}", testId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
            .andExpect(status().isBadRequest)
            .andReturn().response.status

        // Then
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should delete book by id`() {
        // Given
        val id = ObjectId()
        testId = id.toString()

        val library = LibraryDataFactory.createMongoLibrary(id)
        mongoTemplate.save(library)

        val expected = 204

        // When
        val actual = mockMvc.perform(delete("${URL}/{id}", testId))
            .andExpect(status().isNoContent)
            .andReturn().response.status

        // Then
        Assertions.assertEquals(expected, actual)
    }

    companion object {
        const val URL = "/api/v1/library"
    }
}
