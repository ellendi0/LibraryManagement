package com.example.librarymanagement.model.jpa

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "reservation")
data class JpaReservation (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long ?= null,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: JpaUser,

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    val book: JpaBook,

    @ManyToOne
    @JoinColumn(name = "library_id")
    val library: JpaLibrary?= null,
)
