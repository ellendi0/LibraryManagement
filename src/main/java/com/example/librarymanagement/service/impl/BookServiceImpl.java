package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.exception.DuplicateKeyException;
import com.example.librarymanagement.exception.EntityNotFoundException;
import com.example.librarymanagement.model.entity.Book;
import com.example.librarymanagement.model.entity.BookPresence;
import com.example.librarymanagement.model.entity.Reservation;
import com.example.librarymanagement.repository.BookRepository;
import com.example.librarymanagement.service.AuthorService;
import com.example.librarymanagement.service.BookPresenceService;
import com.example.librarymanagement.service.BookService;
import com.example.librarymanagement.service.PublisherService;
import com.example.librarymanagement.service.ReservationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookPresenceService bookPresenceService;
    private final ReservationService reservationService;
    private final AuthorService authorService;
    private final PublisherService publisherService;

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Book"));
    }

    @Override
    public Book getBookByTitleAndAuthor(String title, Long authorId) {
        return bookRepository.findBookByTitleAndAuthorId(title, authorId).orElseThrow(
                () -> new EntityNotFoundException("Book")
        );
    }

    @Override
    @Transactional
    public Book createBook(Long authorId, Long publisherId, Book book) {
        if(bookRepository.existsByIsbn(book.getIsbn())){
            throw new DuplicateKeyException("Book", "ISBN");
        }
        book.setAuthor(authorService.getAuthorById(authorId));
        book.setPublisher(publisherService.getPublisherById(publisherId));
        return bookRepository.save(book);
    }

    @Override
    public Book updateBook(Long id, Book updatedBook) {
        Book book = getBookById(id);

        book.setTitle(updatedBook.getTitle());
        book.setPublishedYear(updatedBook.getPublishedYear());
        book.setGenre(updatedBook.getGenre());

        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        bookRepository.findById(id)
                .ifPresent(book -> {
                    List<BookPresence> bookPresences = book.getBookPresence();
                    List<Reservation> reservations = book.getReservations();

                    if (!CollectionUtils.isEmpty(bookPresences) && !CollectionUtils.isEmpty(reservations)) {
                        bookPresences.forEach(bookPresence -> bookPresenceService.deleteBookPresenceById(bookPresence.getId()));
                        reservations.forEach(reservation -> reservationService.deleteReservationById(reservation.getId()));
                    }

                    bookRepository.delete(book);
        });
    }
}