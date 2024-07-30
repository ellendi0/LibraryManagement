package com.example.librarymanagement.model.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import lombok.EqualsAndHashCode
import lombok.ToString

@Entity
@Table(name = "user")
@ToString(exclude = ["journals", "reservations"])
@EqualsAndHashCode(exclude = ["journals", "reservations"])
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long ?= null,

    @Column(name = "first_name", nullable = false)
    var firstName: String,
    @Column(name = "last_name", nullable = false)
    var lastName: String,

    @Column(unique = true, nullable = false)
    var email: String,

    @Column(nullable = false)
    var password: String,

    @Column(unique = true, length = 10, nullable = false)
    var phoneNumber: String,

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "user", orphanRemoval = true)
    var journals: MutableList<Journal> = mutableListOf(),

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "user", orphanRemoval = true)
    var reservations: MutableList<Reservation> = mutableListOf(),
)