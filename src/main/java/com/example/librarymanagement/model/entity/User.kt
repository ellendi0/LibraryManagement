package com.example.librarymanagement.model.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "user")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long ?= null,

    var firstName: String = "",
    var lastName: String = "",

    @Column(unique = true)
    var email: String = "",

    var password: String = "",

    @Column(unique = true, length = 10)
    var phoneNumber: String = "",

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "user", orphanRemoval = true)
    var journals: MutableList<Journal> ?= null,

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "user", orphanRemoval = true)
    var reservations: MutableList<Reservation> ?= null,
)