package com.example.librarymanagement.services;

import com.example.librarymanagement.models.entities.User;
import java.util.List;

public interface IUserService {
    User getUserByEmail(String email);
    User getUserByNumber(String number);
    User getUserById(Long id);
    User createUser(User user);
    User updateUser(Long id, User updatedUser);
    List<User> findAll();
    void deleteUser(Long id);
}