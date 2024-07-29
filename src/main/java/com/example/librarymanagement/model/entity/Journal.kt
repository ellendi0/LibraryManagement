package com.example.librarymanagement.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import lombok.EqualsAndHashCode
import java.time.LocalDate

@Entity
@Table(name = "journal")
@EqualsAndHashCode(exclude = ["journals", "reservations"])
data class Journal(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long ?= null,

    @Column(name = "date_of_borrowing")
    var dateOfBorrowing: LocalDate,

    @Column(name = "date_of_returning")
    var dateOfReturning: LocalDate ?= null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User ?= null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookPresence_id", nullable = false)
    var bookPresence: BookPresence ?= null,
)