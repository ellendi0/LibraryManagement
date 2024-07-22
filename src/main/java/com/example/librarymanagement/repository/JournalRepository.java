package com.example.librarymanagement.repository;

import com.example.librarymanagement.model.entity.Journal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JournalRepository extends JpaRepository<Journal, Long> {
    Optional<Journal> findByBookPresenceIdAndUserId(Long bookPresenceId, Long userId);
    List<Journal> findByBookPresenceId(Long bookPresenceId);
    List<Journal> findAllByUserId(Long userId);
}