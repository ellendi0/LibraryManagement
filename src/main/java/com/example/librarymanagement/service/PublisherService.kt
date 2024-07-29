package com.example.librarymanagement.service

import com.example.librarymanagement.model.entity.Publisher

interface PublisherService {
    fun createPublisher(publisher: Publisher): Publisher
    fun updatePublisher(id: Long, updatedPublisher: Publisher): Publisher
    fun getPublisherById(id: Long): Publisher
    fun getAllPublishers(): List<Publisher>
}