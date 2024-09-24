package com.example.librarymanagement.configuration

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@EnableAutoConfiguration(
    exclude = [
        MongoAutoConfiguration::class,
        MongoDataAutoConfiguration::class
    ])
@Profile("jpa")
class JpaConfiguration
