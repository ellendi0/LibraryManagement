package com.example.librarymanagement.model.entity

import com.example.librarymanagement.model.enums.Genre
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
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
@Table(name = "book")
@ToString(exclude = ["bookPresence", "reservations"])
data class Book (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var title: String,

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    var author: Author ?= null,

    @ManyToOne
    @JoinColumn(name = "publisher_id", nullable = false)
    var publisher: Publisher ?= null,

    @Column(nullable = false)
    var publishedYear: Int,

    @Column(nullable = false)
    var isbn: Long,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var genre: Genre,

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "book")
    var bookPresence: MutableList<BookPresence> = mutableListOf(),

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "book")
    var reservations: MutableList<Reservation> = mutableListOf()
)