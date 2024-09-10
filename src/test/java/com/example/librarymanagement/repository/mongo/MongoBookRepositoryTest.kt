package com.example.librarymanagement.repository.mongo

import com.example.librarymanagement.data.BookDataFactory
import com.example.librarymanagement.model.mongo.MongoBook
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.data.mongodb.core.MongoTemplate

class MongoBookRepositoryTest {
    private val mongoTemplate: MongoTemplate = mockk()
    private val mongoBookRepository = MongoBookRepository(mongoTemplate)
    private val mongoBook = BookDataFactory.createMongoBook()

    private val mongoId = BookDataFactory.MONGO_ID
    private val id = mongoId.toString()

    private val book = BookDataFactory.createBook(id)

    @Test
    fun `should save book`() {
        //GIVEN
        val expected = book

        every { mongoTemplate.save(mongoBook) } returns mongoBook

        //WHEN
        val actual = mongoBookRepository.save(book)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { mongoTemplate.save(mongoBook) }
    }

    @Test
    fun `should find by id`() {
        //GIVEN
        val expected = book

        every { mongoTemplate.findById(any(), MongoBook::class.java) } returns mongoBook

        //WHEN
        val actual = mongoBookRepository.findById(id)

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should find all books`() {
        //GIVEN
        val expected = listOf(book)

        every { mongoTemplate.findAll(MongoBook::class.java) } returns listOf(mongoBook)

        //WHEN
        val actual = mongoBookRepository.findAll()

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should delete book by id`() {
        //GIVEN
        every { mongoTemplate.findAndRemove(any(), MongoBook::class.java) } returns mongoBook

        //WHEN
        mongoBookRepository.deleteById(id)

        //THEN
        verify(exactly = 1) { mongoTemplate.findAndRemove(any(), MongoBook::class.java) }
    }

    @Test
    fun `should return true if exists by isbn`() {
        //GIVEN
        val isbn = 1111111111111L
        val expected = true

        every { mongoTemplate.exists(any(), MongoBook::class.java) } returns true

        //WHEN
        val actual = mongoBookRepository.existsByIsbn(isbn)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { mongoTemplate.exists(any(), MongoBook::class.java) }
    }

    @Test
    fun `should find book by title and authorId`() {
        //GIVEN
        val expected = book
        val title = book.title

        every { mongoTemplate.findOne(any(), MongoBook::class.java) } returns mongoBook

        //WHEN
        val actual = mongoBookRepository.findBookByTitleAndAuthorId(title, id)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) {
            mongoTemplate.findOne(
                match {
                    it.queryObject[MongoBook::title.name] == title &&
                            it.queryObject[MongoBook::authorId.name] == mongoId
                },
                MongoBook::class.java
            )
        }
    }
}
