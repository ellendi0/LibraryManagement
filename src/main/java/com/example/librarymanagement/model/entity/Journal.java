package com.example.librarymanagement.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "journal")
public class Journal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "date_of_borrowing")
    private LocalDate dateOfBorrowing;

    @Column(name = "date_of_returning")
    private LocalDate dateOfReturning;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookPresence_id")
    private BookPresence bookPresence;

    public Journal(User user, BookPresence bookPresence) {
        this.dateOfBorrowing = LocalDate.now();
        this.user = user;
        this.bookPresence = bookPresence;
    }
}