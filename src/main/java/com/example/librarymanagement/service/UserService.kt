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
    fun findReservationsByUser(userId: String): List<Reservation>
    fun findJournalsByUser(userId: String): List<Journal>
    fun borrowBookFromLibrary(userId: String, libraryId: String, bookId: String): List<Journal>
    fun reserveBookInLibrary(userId: String, libraryId: String, bookId: String): List<Reservation>
    fun cancelReservationInLibrary(userId: String, bookId: String)
    fun returnBookToLibrary(userId: String, libraryId: String, bookId: String)
    fun deleteUser(id: String)
}
