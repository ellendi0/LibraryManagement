package com.example.librarymanagement.controller;

import com.example.librarymanagement.dto.BookPresenceDto;
import com.example.librarymanagement.dto.LibraryDto;
import com.example.librarymanagement.dto.mapper.BookPresenceMapper;
import com.example.librarymanagement.dto.mapper.LibraryMapper;
import com.example.librarymanagement.model.entity.Library;
import com.example.librarymanagement.model.enums.Availability;
import com.example.librarymanagement.service.LibraryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/library")
public class LibraryController {
    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping
    public ResponseEntity<List<LibraryDto>> getAllLibraries() {
        return new ResponseEntity<>(LibraryMapper.toLibraryDto(libraryService.findAll()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibraryDto> getById(@PathVariable Long id) {
        return new ResponseEntity<>(new LibraryDto(libraryService.getLibraryById(id)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<LibraryDto> createLibrary(@RequestBody @Valid Library library) {
        return new ResponseEntity<>(new LibraryDto(libraryService.createLibrary(library)), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LibraryDto> updateLibrary(@PathVariable Long id, @RequestBody @Valid Library library) {
        return new ResponseEntity<>(new LibraryDto(libraryService.updateLibrary(id, library)), HttpStatus.OK);
    }

    @PostMapping("/{libraryId}/inventory")
    public ResponseEntity<LibraryDto> addBookToLibrary(@PathVariable Long libraryId, @RequestParam Long bookId) {
        return new ResponseEntity<>(new LibraryDto(libraryService.addBookToLibrary(libraryId, bookId)), HttpStatus.OK);
    }

    @GetMapping("/{libraryId}/inventory")
    public ResponseEntity<List<BookPresenceDto>> getAllBooksByLibraryIdAndStatus(@PathVariable Long libraryId,
                                                                                 @RequestParam Availability availability) {
        return new ResponseEntity<>(BookPresenceMapper.toBookPresenceDto(
                libraryService.getAllBooksByLibraryIdAndAvailability(libraryId, availability)), HttpStatus.OK);
    }

    @GetMapping("/{libraryId}/all")
    public ResponseEntity<List<BookPresenceDto>> getAllBooksByLibraryId(@PathVariable Long libraryId) {
        return new ResponseEntity<>(BookPresenceMapper.toBookPresenceDto(
                libraryService.getAllBooksByLibraryId(libraryId)), HttpStatus.OK);
    }

    @DeleteMapping("/{libraryId}/inventory")
    public ResponseEntity<Void> removeBookFromLibrary(@PathVariable Long libraryId, @RequestParam Long bookId) {
        libraryService.removeBookFromLibrary(libraryId, bookId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLibrary(@PathVariable Long id) {
        libraryService.deleteLibrary(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}