package com.example.librarymanagement.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "library")
data class Library (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long ?= null,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var address: String,

    @OneToMany(mappedBy = "library")
    var bookPresence: MutableList<BookPresence> = mutableListOf()
)