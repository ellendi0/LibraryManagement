package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.exception.EntityNotFoundException;
import com.example.librarymanagement.model.entity.Journal;
import com.example.librarymanagement.repository.JournalRepository;
import com.example.librarymanagement.service.JournalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JournalServiceImpl implements JournalService {
    private final JournalRepository journalRepository;

    @Override
    public Journal createJournal(Journal journal) {
        return journalRepository.save(journal);
    }

    @Override
    public Journal updateJournal(Long id, Journal updatedJournal) {
        Journal journal = getJournalById(id);
        journal.setBookPresence(updatedJournal.getBookPresence());
        journal.setUser(updatedJournal.getUser());
        journal.setDateOfBorrowing(updatedJournal.getDateOfBorrowing());
        journal.setDateOfReturning(updatedJournal.getDateOfReturning());
        return journalRepository.save(journal);
    }

    @Override
    public Journal findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(Long bookPresenceId, Long userId) {
        return journalRepository.findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(bookPresenceId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Journal"));
    }

    @Override
    public Journal getJournalById(Long id) {
        return journalRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Journal"));
    }

    @Override
    public List<Journal> getJournalByBookPresenceIdAndUserId(Long bookPresenceId, Long userId) {
        return journalRepository.findByBookPresenceIdAndUserId(bookPresenceId, userId);
    }

    @Override
    public List<Journal> getJournalByUserId(Long userId) {
        return journalRepository.findAllByUserId(userId);
    }

    @Override
    public void deleteJournal(Long id) {
        journalRepository.findById(id).ifPresent(journalRepository::delete);
    }
}