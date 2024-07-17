package com.example.librarymanagement.repository;

import com.example.librarymanagement.model.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByBookIdAndUserId(Long bookId, Long userId);
    List<Reservation> findAllByLibraryId(Long libraryId);
    List<Reservation> findAllByUserId(Long userId);
    Reservation findFirstByBookIdAndLibraryIdOrLibraryIsNull(Long bookId, Long libraryId);
}