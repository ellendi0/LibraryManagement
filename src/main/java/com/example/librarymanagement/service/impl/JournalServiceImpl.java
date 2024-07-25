package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.exception.EntityNotFoundException;
import com.example.librarymanagement.model.entity.Journal;
import com.example.librarymanagement.repository.JournalRepository;
import com.example.librarymanagement.service.JournalService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JournalServiceImpl implements JournalService {
    private final JournalRepository journalRepository;

    public JournalServiceImpl(JournalRepository journalRepository) {
        this.journalRepository = journalRepository;
    }

    @Override
    public Journal createJournal(Journal journal) {
        return journalRepository.save(journal);
    }

    @Override
    public Journal getJournalById(Long id) {
        return journalRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Journal"));
    }

    @Override
    public List<Journal> getJournalByBookPresenceId(Long bookPresenceId) {
        return journalRepository.findByBookPresenceId(bookPresenceId);
    }

    @Override
    public Journal getJournalByBookPresenceIdAndUserId(Long bookPresenceId, Long userId) {
        return journalRepository.findByBookPresenceIdAndUserId(bookPresenceId, userId).orElseThrow(
                () -> new EntityNotFoundException("Journal"));
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