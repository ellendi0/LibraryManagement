package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.model.entity.Author;
import com.example.librarymanagement.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceImplTest {
    @InjectMocks
    private AuthorServiceImpl authorServiceImpl;

    @Mock
    private AuthorRepository authorRepository;

    private static Author author1;
    private static Author author2;

    @BeforeAll
    public static void init() {
        author1 = new Author();
        author1.setFirstName("Author1");
        author1.setLastName("Author1");

        author2 = new Author();
        author2.setFirstName("Author2");
        author2.setLastName("Author2");
    }

    @Test
    public void findAll() {
        when(authorRepository.findAll()).thenReturn(List.of(author1, author2));

        assertEquals(List.of(author1, author2), authorServiceImpl.getAllAuthors());
        verify(authorRepository, times(1)).findAll();
    }

    @Test
    public void getAuthorById() {
        when(authorRepository.findById(1L)).thenReturn(java.util.Optional.of(author1));

        assertEquals(author1, authorServiceImpl.getAuthorById(1L));
        verify(authorRepository, times(1)).findById(1L);
    }

    @Test
    public void createAuthor() {
        when(authorRepository.save(author1)).thenReturn(author1);

        assertEquals(author1, authorServiceImpl.createAuthor(author1));
        verify(authorRepository, times(1)).save(author1);
    }

    @Test
    public void updateAuthor() {
        when(authorRepository.findById(1L)).thenReturn(java.util.Optional.of(author1));
        when(authorRepository.save(author1)).thenReturn(author1);

        assertEquals(author1, authorServiceImpl.updateAuthor(1L, author1));
        verify(authorRepository, times(1)).findById(1L);
        verify(authorRepository, times(1)).save(author1);
    }
}