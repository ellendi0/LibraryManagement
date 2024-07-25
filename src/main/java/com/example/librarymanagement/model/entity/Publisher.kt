package com.example.librarymanagement.model.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "publisher")
data class Publisher (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long ?= null,

    var name: String = "",

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "publisher")
    var books: MutableList<Book> = mutableListOf()
)