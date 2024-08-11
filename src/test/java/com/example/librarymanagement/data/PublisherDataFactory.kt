package com.example.librarymanagement.data

import com.example.librarymanagement.model.entity.Publisher

object PublisherDataFactory {
    fun createPublisher(): Publisher = Publisher(1L, "Publisher")
}
