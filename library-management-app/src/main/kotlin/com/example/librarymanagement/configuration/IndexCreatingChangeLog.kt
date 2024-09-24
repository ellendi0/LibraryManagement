//package com.example.librarymanagement.configuration
//
//import com.example.librarymanagement.model.mongo.MongoBook
//import com.example.librarymanagement.model.mongo.MongoUser
//import org.springframework.data.domain.Sort
//import org.springframework.data.mongodb.core.index.Index
//import org.springframework.data.mongodb.core.MongoTemplate
//
//@ChangeUnit(id = "index-creating-changelog", order = "001")
//class IndexCreatingChangeLog(
//    private val mongoTemplate: MongoTemplate
//) {
//    @Execution
//    fun createIndex() {
//        mongoTemplate.indexOps(MongoBook::class.java)
//            .ensureIndex(Index().on(MongoBook::isbn.name, Sort.Direction.ASC))
//        mongoTemplate.indexOps(MongoUser::class.java)
//            .ensureIndex(Index().on(MongoUser::email.name, Sort.Direction.ASC))
//    }
//}
