package com.example.librarymanagement.model.jpa

import com.example.librarymanagement.model.enums.Availability
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
import lombok.ToString

@Entity
@Table(name = "presence_of_book")
data class JpaBookPresence (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long ?= null,

    @Enumerated(EnumType.STRING)
    var availability: Availability = Availability.AVAILABLE,

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    val book: JpaBook,

    @ManyToOne
    @JoinColumn(name = "library_id", nullable = false)
    val library: JpaLibrary,

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: JpaUser? = null,

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "bookPresence")
    val journals: MutableList<JpaJournal> = mutableListOf()
)
