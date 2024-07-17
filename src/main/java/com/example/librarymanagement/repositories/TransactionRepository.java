package com.example.librarymanagement.repositories;

import com.example.librarymanagement.models.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
