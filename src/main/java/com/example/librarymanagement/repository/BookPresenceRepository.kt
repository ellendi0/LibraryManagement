package com.example.librarymanagement.repository;

import com.example.librarymanagement.model.entity.BookPresence;
import com.example.librarymanagement.model.entity.User;
import com.example.librarymanagement.model.enums.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookPresenceRepository extends JpaRepository<BookPresence, Long> {
    List<BookPresence> findAllByBookId(Long bookId);
    List<BookPresence> findAllByLibraryId(Long libraryId);
    List<BookPresence> findAllByUserId(Long userId);
    List<BookPresence> findAllByLibraryIdAndBookId(Long libraryId, Long bookId);
    Optional<BookPresence> findAllByLibraryIdAndBookIdAndUser(Long libraryId, Long bookId, User user);
    List<BookPresence> findAllByLibraryIdAndBookIdAndAvailability(Long libraryId, Long bookId, Availability availability);
    List<BookPresence> findAllByLibraryIdAndAvailability(Long libraryId, Availability availability);
    void deleteBookPresenceByIdAndLibraryId(Long bookId, Long libraryId);
}