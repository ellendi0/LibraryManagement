package com.example.librarymanagement.model.jpa

import com.example.librarymanagement.model.jpa.JpaLibrary.Companion.TABLE_NAME
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = TABLE_NAME)
data class JpaLibrary(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val address: String,

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "library", orphanRemoval = true)
    val bookPresence: MutableList<JpaBookPresence> = mutableListOf()
) {
    companion object {
        const val TABLE_NAME = "library"
    }
}
