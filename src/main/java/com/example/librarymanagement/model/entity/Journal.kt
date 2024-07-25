package com.example.librarymanagement.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "journal")
data class Journal(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long ?= null,

    @Column(name = "date_of_borrowing")
    var dateOfBorrowing: LocalDate = LocalDate.now(),

    @Column(name = "date_of_returning")
    var dateOfReturning: LocalDate ?= null,

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User ?= null,

    @ManyToOne
    @JoinColumn(name = "bookPresence_id")
    var bookPresence: BookPresence ?= null
) {
    constructor(user: User, bookPresence: BookPresence) : this() {
        this.user = user
        this.bookPresence = bookPresence
    }
}