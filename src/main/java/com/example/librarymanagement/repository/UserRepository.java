package com.example.librarymanagement.repository;

import com.example.librarymanagement.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);
    Boolean existsByPhoneNumber(String phoneNumber);
    Optional<User> findByEmailOrPhoneNumber(String email, String phoneNumber);
}