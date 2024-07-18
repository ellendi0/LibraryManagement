package com.example.librarymanagement.models.entities;

import com.example.librarymanagement.models.enums.Availability;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Availability availability;

    @OneToMany(mappedBy = "inventory", cascade = CascadeType.REMOVE)
    private List<Book> books;
}