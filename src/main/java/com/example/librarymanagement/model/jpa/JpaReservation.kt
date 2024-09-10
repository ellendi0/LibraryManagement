package com.example.librarymanagement.model.jpa

import com.example.librarymanagement.model.jpa.JpaReservation.Companion.TABLE_NAME
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = TABLE_NAME)
data class JpaReservation(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    val user: JpaUser?,

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    val book: JpaBook?,

    @ManyToOne
    @JoinColumn(name = "library_id")
    val library: JpaLibrary? = null
) {
    companion object {
        const val TABLE_NAME = "reservation"
    }
}
