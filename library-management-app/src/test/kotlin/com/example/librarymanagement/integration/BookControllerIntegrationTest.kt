package com.example.librarymanagement.integration

import com.example.librarymanagement.data.AuthorDataFactory
import com.example.librarymanagement.data.BookDataFactory
import com.example.librarymanagement.data.PublisherDataFactory
import com.example.librarymanagement.dto.mapper.BookMapper
import com.example.librarymanagement.model.mongo.MongoAuthor
import com.example.librarymanagement.model.mongo.MongoBook
import com.example.librarymanagement.model.mongo.MongoPublisher
import com.example.librarymanagement.repository.mongo.mapper.MongoBookMapper
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class BookControllerIntegrationTest: AbstractIntegrationTest(){
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var bookMapper: BookMapper

    @Autowired
    private lateinit var mongoTemplate: MongoTemplate

    private lateinit var testId: String
    
    @AfterEach
    fun cleanUp() {
        mongoTemplate.findAndRemove(Query(Criteria.where("id").`is`(testId)), MongoBook::class.java)
        mongoTemplate.findAndRemove(Query(Criteria.where("id").`is`(testId)), MongoPublisher::class.java)
        mongoTemplate.findAndRemove(Query(Criteria.where("id").`is`(testId)), MongoAuthor::class.java)
    }

    @Test
    fun `should create book`() {
        // Given
        val id = ObjectId()
        testId = id.toString()

        val publisher = PublisherDataFactory.createMongoPublisher(id)
        val author = AuthorDataFactory.createMongoAuthor(id)
        mongoTemplate.save(publisher)
        mongoTemplate.save(author)

        val book = BookDataFactory.createMongoBook(id)
        val bookRequest = BookDataFactory.createBookRequestDto(testId).copy(publisherId = testId, authorId = testId)
        val bookDto = bookMapper.toBookDto(MongoBookMapper.toDomain(book))
        val content = objectMapper.writeValueAsString(bookRequest)
        val expected = objectMapper.writeValueAsString(bookDto)

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
    fun `should not create book with invalid data`() {
        // Given
        val id = ObjectId()
        testId = id.toString()

        val publisher = PublisherDataFactory.createMongoPublisher(id)
        val author = AuthorDataFactory.createMongoAuthor(id)
        mongoTemplate.save(publisher)
        mongoTemplate.save(author)

        val bookRequest = BookDataFactory.createBookRequestDto(testId).copy(publisherId = "", authorId = testId)
        val content = objectMapper.writeValueAsString(bookRequest)
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
    fun `should update book`() {
        // Given
        val id = ObjectId()
        testId = id.toString()

        val publisher = PublisherDataFactory.createMongoPublisher(id)
        val author = AuthorDataFactory.createMongoAuthor(id)
        mongoTemplate.save(publisher)
        mongoTemplate.save(author)

        val book = BookDataFactory.createMongoBook(id)
        mongoTemplate.save(book)

        val bookRequest = BookDataFactory.createBookRequestDto(testId).copy(
            title = "Test title",
            publisherId = testId,
            authorId = testId)

        val updatedBook = bookMapper.toBook(bookRequest, testId)
        val updatedBookDto = bookMapper.toBookDto(updatedBook)
        val content = objectMapper.writeValueAsString(bookRequest)
        val expected = objectMapper.writeValueAsString(updatedBookDto)

        // When
        val actual = mockMvc.perform(
            put("$URL/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        // Then
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should not update book`() {
        // Given
        val id = ObjectId()
        testId = id.toString()

        val publisher = PublisherDataFactory.createMongoPublisher(id)
        val author = AuthorDataFactory.createMongoAuthor(id)
        mongoTemplate.save(publisher)
        mongoTemplate.save(author)

        val book = BookDataFactory.createMongoBook(id)
        mongoTemplate.save(book)

        val bookRequest = BookDataFactory.createBookRequestDto(testId).copy(
            title = "",
            publisherId = testId,
            authorId = testId)

        val content = objectMapper.writeValueAsString(bookRequest)
        val expected = 400

        // When
        val actual = mockMvc.perform(
            put("$URL/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
            .andExpect(status().isBadRequest)
            .andReturn().response.status

        // Then
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should get book by id`() {
        // Given
        val id = ObjectId()
        testId = id.toString()

        val book = BookDataFactory.createMongoBook(id)
        mongoTemplate.save(book)

        val expected = objectMapper.writeValueAsString(bookMapper.toBookDto(MongoBookMapper.toDomain(book)))

        // When
        val actual = mockMvc.perform(get("$URL/{id}", testId))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        // Then
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should get all book`() {
        // Given
        val id = ObjectId()
        testId = id.toString()

        val book = BookDataFactory.createMongoBook(id)
        mongoTemplate.save(book)

        val expected = objectMapper.writeValueAsString(listOf(bookMapper.toBookDto(MongoBookMapper.toDomain(book))))

        // When
        val actual = mockMvc.perform(get(URL))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        // Then
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should delete book by id`() {
        // Given
        val id = ObjectId()
        testId = id.toString()

        val book = BookDataFactory.createMongoBook(id)
        mongoTemplate.save(book)

        val expected = 204

        // When
        val actual = mockMvc.perform(delete("$URL/{ID}", testId))
            .andExpect(status().isNoContent)
            .andReturn().response.status

        // Then
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should get book by title and author`() {
        // Given
        val id = ObjectId()
        testId = id.toString()

        val book = BookDataFactory.createMongoBook(id).copy(authorId = id)
        mongoTemplate.save(book)

        val expected = objectMapper.writeValueAsString(listOf(bookMapper.toBookDto(MongoBookMapper.toDomain(book))))

        // When
        val actual = mockMvc.perform(get(URL)
            .param("title", book.title)
            .param("authorId", testId)
        )
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        // Then
        Assertions.assertEquals(expected, actual)
    }

    companion object{
        const val URL = "/api/v1/book"
    }
}
