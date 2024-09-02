package com.example.librarymanagement.model.jpa

import com.example.librarymanagement.model.jpa.JpaAuthor.Companion.TABLE_NAME
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = TABLE_NAME)
data class JpaAuthor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "first_name", nullable = false)
    val firstName: String,

    @Column(name = "last_name", nullable = false)
    val lastName: String
) {
    companion object {
        const val TABLE_NAME = "author"
    }
}
