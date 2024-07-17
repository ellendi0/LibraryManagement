package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.data.TestDataFactory;
import com.example.librarymanagement.model.entity.Author;
import com.example.librarymanagement.model.entity.Book;
import com.example.librarymanagement.model.entity.Publisher;
import com.example.librarymanagement.repository.BookRepository;
import com.example.librarymanagement.service.BookPresenceService;
import com.example.librarymanagement.service.ReservationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

    @InjectMocks
    private BookServiceImpl bookServiceImpl;

    @Mock
    private BookRepository bookRepository;
    @Mock
    private AuthorServiceImpl authorServiceImpl;
    @Mock
    private PublisherServiceImpl publisherServiceImpl;
    @Mock
    private BookPresenceService bookPresenceService;
    @Mock
    private ReservationService reservationService;

    private static Author author1;
    private static Publisher publisher;
    private static Book book1;

    @BeforeAll
    public static void init(){
        TestDataFactory.TestDataRel testDataRel = TestDataFactory.createTestDataRel();

        book1 = testDataRel.book;
    }

    @Test
    public void findAll() {
        when(bookRepository.findAll()).thenReturn(List.of(book1));

        assertEquals(List.of(book1), bookServiceImpl.findAll());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    public void getBookById() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));

        assertEquals(book1, bookServiceImpl.getBookById(1L));
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    public void getBookByTitleAndAuthor() {
        when(bookRepository.findBookByTitleAndAuthorId("Book1", 1L)).thenReturn(Optional.of(book1));

        assertEquals(book1, bookServiceImpl.getBookByTitleAndAuthor("Book1", 1L));
        verify(bookRepository, times(1)).findBookByTitleAndAuthorId("Book1", 1L);
    }

    @Test
    public void createBook() {
        when(authorServiceImpl.getAuthorById(1L)).thenReturn(author1);
        when(publisherServiceImpl.getPublisherById(1L)).thenReturn(publisher);
        when(bookRepository.save(book1)).thenReturn(book1);

        assertEquals(book1, bookServiceImpl.createBook(1L, 1L, book1));
        verify(bookRepository, times(1)).save(book1);
    }

    @Test
    public void updateBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        when(bookRepository.save(book1)).thenReturn(book1);

        assertEquals(book1, bookServiceImpl.updateBook(1L, book1));
        verify(bookRepository, times(1)).save(book1);
    }

    @Test
    public void deleteBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        doNothing().when(bookRepository).delete(book1);

        bookServiceImpl.deleteBook(1L);
        verify(bookRepository, times(1)).delete(book1);
        verify(bookRepository, times(1)).findById(1L);
    }
}