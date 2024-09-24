//package com.example.librarymanagement.repository.mongo
//
//import com.example.librarymanagement.data.LibraryDataFactory
//import com.example.librarymanagement.model.mongo.MongoLibrary
//import io.mockk.every
//import io.mockk.mockk
//import io.mockk.verify
//import org.junit.jupiter.api.Assertions
//import org.junit.jupiter.api.Test
//import org.springframework.data.mongodb.core.MongoTemplate
//
//class MongoLibraryRepositoryTest {
//    private val mongoTemplate: MongoTemplate = mockk()
//    private val mongoLibraryRepository = MongoLibraryRepository(mongoTemplate)
//
//    private val mongoId = LibraryDataFactory.MONGO_ID
//    private val id = mongoId.toString()
//
//    private val library = LibraryDataFactory.createLibrary(id)
//    private val mongoLibrary = LibraryDataFactory.createMongoLibrary(mongoId)
//
//    @Test
//    fun `should find all libraries`() {
//        //GIVEN
//        val expected = listOf(library)
//
//        every { mongoTemplate.findAll(MongoLibrary::class.java) } returns listOf(mongoLibrary)
//
//        //WHEN
//        val actual = mongoLibraryRepository.findAll()
//
//        //THEN
//        Assertions.assertEquals(expected, actual)
//    }
//
//    @Test
//    fun `should save library`() {
//        //GIVEN
//        val expected = library
//
//        every { mongoTemplate.save(mongoLibrary) } returns mongoLibrary
//
//        //WHEN
//        val actual = mongoLibraryRepository.save(expected)
//
//        //THEN
//        Assertions.assertEquals(expected, actual)
//        verify(exactly = 1) { mongoTemplate.save(mongoLibrary) }
//    }
//
//    @Test
//    fun `should find library by id`() {
//        //GIVEN
//        val expected = library
//
//        every { mongoTemplate.findById(mongoId, MongoLibrary::class.java) } returns mongoLibrary
//
//        //WHEN
//        val actual = mongoLibraryRepository.findById(id)
//
//        //THEN
//        Assertions.assertEquals(expected, actual)
//    }
//
//    @Test
//    fun `should delete library by id`() {
//        //GIVEN
//        every { mongoTemplate.findAndRemove(any(), MongoLibrary::class.java) } returns mongoLibrary
//
//        //WHEN
//        mongoLibraryRepository.deleteById(id)
//
//        //THEN
//        verify(exactly = 1) { mongoTemplate.findAndRemove(any(), MongoLibrary::class.java) }
//    }
//}
