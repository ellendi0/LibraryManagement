package com.example.librarymanagement.model.jpa

import com.example.librarymanagement.model.jpa.JpaPublisher.Companion.TABLE_NAME
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = TABLE_NAME)
data class JpaPublisher(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val name: String
) {
    companion object {
        const val TABLE_NAME = "publisher"
    }
}
