package com.example.librarymanagement.service

import com.example.librarymanagement.model.entity.Journal
import com.example.librarymanagement.model.entity.Reservation
import com.example.librarymanagement.model.entity.User

interface UserService {
    fun getUserByPhoneNumberOrEmail(email: String?, phoneNumber: String?): User
    fun getUserById(id: Long): User
    fun createUser(user: User): User
    fun updateUser(updatedUser: User): User
    fun findAll(): List<User>
    fun findReservationsByUser(userId: Long): List<Reservation>
    fun findJournalsByUser(userId: Long): List<Journal>
    fun borrowBookFromLibrary(userId: Long, libraryId: Long, bookId: Long): List<Journal>
    fun reserveBookInLibrary(userId: Long, libraryId: Long, bookId: Long): List<Reservation>
    fun cancelReservationInLibrary(userId: Long, bookId: Long)
    fun returnBookToLibrary(userId: Long, libraryId: Long, bookId: Long): List<Journal>
    fun deleteUser(id: Long)
}
