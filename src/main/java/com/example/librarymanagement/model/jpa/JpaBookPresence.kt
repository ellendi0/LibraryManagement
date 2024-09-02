package com.example.librarymanagement.model.jpa

import com.example.librarymanagement.model.enums.Availability
import com.example.librarymanagement.model.jpa.JpaBookPresence.Companion.TABLE_NAME
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
@Table(name = TABLE_NAME)
data class JpaBookPresence(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Enumerated(EnumType.STRING)
    var availability: Availability = Availability.AVAILABLE,

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    val book: JpaBook?,

    @ManyToOne
    @JoinColumn(name = "library_id", nullable = false)
    val library: JpaLibrary?,

    @ManyToOne
    @JoinColumn(name = "users_id")
    var user: JpaUser?,

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "bookPresence", orphanRemoval = true)
    val journals: MutableList<JpaJournal> = mutableListOf()
) {
    companion object {
        const val TABLE_NAME = "presence_of_book"
    }
}
