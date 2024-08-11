package com.example.librarymanagement.repository

import com.example.librarymanagement.model.entity.Journal
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface JournalRepository : JpaRepository<Journal, Long> {
    fun findByBookPresenceIdAndUserId(bookPresenceId: Long, userId: Long): List<Journal>
    fun findAllByUserId(userId: Long): List<Journal>
    fun findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(bookPresenceId: Long, userId: Long): Journal?
}
