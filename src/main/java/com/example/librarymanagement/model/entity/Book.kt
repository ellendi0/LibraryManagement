package com.example.librarymanagement.model.entity

import com.example.librarymanagement.model.enums.Genre
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "book")
data class Book (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long ?= null,

    var title: String = "",

    @ManyToOne
    @JoinColumn(name = "author_id")
    var author: Author?= null,

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    var publisher: Publisher?= null,

    var publishedYear: Int = 0,
    var isbn: Long = 0,

    @Enumerated(EnumType.STRING)
    var genre: Genre ?= null,

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "book")
    var bookPresence: MutableList<BookPresence> = mutableListOf(),

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "book")
    var reservations: MutableList<Reservation> = mutableListOf()
)