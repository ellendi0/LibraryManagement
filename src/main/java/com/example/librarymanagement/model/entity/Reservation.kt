package com.example.librarymanagement.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "reservation")
data class Reservation (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long ?= null,

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User ?= null,

    @ManyToOne
    @JoinColumn(name = "book_id")
    var book: Book ?= null,

    @ManyToOne
    @JoinColumn(name = "library_id")
    var library: Library ?= null,
) {
    constructor(user: User, book: Book, library: Library) : this() {
        this.user = user
        this.book = book
        this.library = library
    }
}