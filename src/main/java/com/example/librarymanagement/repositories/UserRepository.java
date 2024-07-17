package com.example.librarymanagement.repositories;

import com.example.librarymanagement.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
