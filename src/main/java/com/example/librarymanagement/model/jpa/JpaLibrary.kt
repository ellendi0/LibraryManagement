package com.example.librarymanagement.model.jpa

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "library")
data class JpaLibrary (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long ?= null,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val address: String,

    @OneToMany(mappedBy = "library")
    val bookPresence: MutableList<JpaBookPresence> = mutableListOf()
)