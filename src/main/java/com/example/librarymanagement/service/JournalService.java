package com.example.librarymanagement.service;

import com.example.librarymanagement.model.entity.Journal;

import java.util.List;

public interface JournalService {
    Journal createJournal(Journal journal);
    Journal getJournalById(Long id);
    List<Journal> getJournalByBookPresenceId(Long bookPresenceId);
    Journal getJournalByBookPresenceIdAndUserId(Long bookPresenceId, Long userId);
    List<Journal> getJournalByUserId(Long userId);
    void deleteJournal(Long id);
}