package com.example.librarymanagement.service

interface AvailabilityNotificationService {
    fun notifyUserAboutBookAvailability(bookId: Long, libraryId: Long?)
}