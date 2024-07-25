package com.example.librarymanagement.model.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "author")
data class Author (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long ?= null,

    var firstName: String = "",
    var lastName: String = "",
    var pseudonym: String ?= null,

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "author")
    var books: MutableList<Book> = mutableListOf()
)