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
    @JoinColumn(name = "user_id", nullable = false)
    val user: User ?= null,

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    val book: Book ?= null,

    @ManyToOne
    @JoinColumn(name = "library_id")
    val library: Library ?= null,
)
