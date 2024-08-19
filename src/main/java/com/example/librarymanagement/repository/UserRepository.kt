package com.example.librarymanagement.repository

import com.example.librarymanagement.model.domain.User

interface UserRepository {
    fun save(user: User): User
    fun findById(userId: String): User?
    fun findAll(): List<User>
    fun deleteUser(userId: String)
    fun existsByEmail(email: String): Boolean
    fun existsByPhoneNumber(phoneNumber: String): Boolean
    fun findByEmailOrPhoneNumber(email: String?, phoneNumber: String?): User?
}
