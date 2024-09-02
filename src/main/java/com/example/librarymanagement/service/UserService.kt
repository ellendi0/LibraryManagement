package com.example.librarymanagement.service

import com.example.librarymanagement.model.domain.Journal
import com.example.librarymanagement.model.domain.Reservation
import com.example.librarymanagement.model.domain.User

interface UserService {
    fun getUserByPhoneNumberOrEmail(email: String?, phoneNumber: String?): User
    fun getUserById(id: String): User
    fun createUser(user: User): User
    fun updateUser(updatedUser: User): User
    fun findAll(): List<User>
    fun findJournalsByUser(userId: String): List<Journal>
//    fun deleteUserById(id: String)
}
