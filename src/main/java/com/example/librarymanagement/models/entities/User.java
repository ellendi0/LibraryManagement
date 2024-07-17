package com.example.librarymanagement.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String lastName;

    @Column(unique = true)
    @Email(message = "Please enter a valid e-mail address")
    private String email;

    @Size(min = 6, max = 20)
    private String password;

    @Column(unique = true, length = 10)
    private String phoneNumber;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Transaction> transactions;
}