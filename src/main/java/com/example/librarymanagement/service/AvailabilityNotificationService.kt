package com.example.librarymanagement.service

interface AvailabilityNotificationService {
    fun notifyUserAboutBookAvailability(bookId: String, libraryId: String?)
}