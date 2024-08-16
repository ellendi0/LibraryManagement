package com.example.librarymanagement.model.jpa

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
data class JpaBook (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val title: String,

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    var author: JpaAuthor?= null,

    @ManyToOne
    @JoinColumn(name = "publisher_id", nullable = false)
    var publisher: JpaPublisher?= null,

    @Column(nullable = false)
    val publishedYear: Int,

    @Column(nullable = false)
    val isbn: Long,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val genre: Genre,

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "book")
    val bookPresence: MutableList<JpaBookPresence> = mutableListOf(),

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "book")
    val reservations: MutableList<JpaReservation> = mutableListOf()
)
