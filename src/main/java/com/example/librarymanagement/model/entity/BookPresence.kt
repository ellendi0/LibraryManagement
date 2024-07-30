package com.example.librarymanagement.model.entity

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
@ToString(exclude = ["journals"])
data class BookPresence (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long ?= null,

    @Enumerated(EnumType.STRING)
    var availability: Availability = Availability.AVAILABLE,

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    var book: Book,

    @ManyToOne
    @JoinColumn(name = "library_id", nullable = false)
    var library: Library,

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User ?= null,

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "bookPresence")
    var journals: MutableList<Journal> = mutableListOf()
)