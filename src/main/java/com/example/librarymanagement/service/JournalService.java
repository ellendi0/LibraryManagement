package com.example.librarymanagement.service;

import com.example.librarymanagement.model.entity.Journal;

import java.util.List;

public interface JournalService {
    Journal createJournal(Journal journal);
    Journal getJournalById(Long id);
    Journal updateJournal(Long id, Journal updatedJournal);
    Journal findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(Long bookPresenceId, Long userId);
    List<Journal> getJournalByBookPresenceIdAndUserId(Long bookPresenceId, Long userId);
    List<Journal> getJournalByUserId(Long userId);
    void deleteJournal(Long id);
}