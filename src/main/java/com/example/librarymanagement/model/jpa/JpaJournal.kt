package com.example.librarymanagement.model.jpa

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "journal")
data class JpaJournal(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long ?= null,

    @Column(name = "date_of_borrowing")
    var dateOfBorrowing: LocalDate,

    @Column(name = "date_of_returning")
    var dateOfReturning: LocalDate ?= null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: JpaUser,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookPresence_id", nullable = false)
    val bookPresence: JpaBookPresence,
)
