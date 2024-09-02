package com.example.librarymanagement.model.jpa

import com.example.librarymanagement.model.jpa.JpaJournal.Companion.TABLE_NAME
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
@Table(name = TABLE_NAME)
data class JpaJournal(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "date_of_borrowing")
    var dateOfBorrowing: LocalDate,

    @Column(name = "date_of_returning")
    var dateOfReturning: LocalDate? = null,

    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    var user: JpaUser?,

    @ManyToOne
    @JoinColumn(name = "presence_of_book_id", nullable = false)
    val bookPresence: JpaBookPresence?
) {
    companion object {
        const val TABLE_NAME = "journal"
    }
}
