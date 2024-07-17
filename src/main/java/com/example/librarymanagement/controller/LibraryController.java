package com.example.librarymanagement.controller;

import com.example.librarymanagement.dto.BookPresenceDto;
import com.example.librarymanagement.dto.LibraryDto;
import com.example.librarymanagement.dto.mapper.BookPresenceMapper;
import com.example.librarymanagement.dto.mapper.LibraryMapper;
import com.example.librarymanagement.model.enums.Availability;
import com.example.librarymanagement.service.LibraryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/library")
@RequiredArgsConstructor
public class LibraryController {
    private final LibraryService libraryService;
    private final LibraryMapper libraryMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<LibraryDto> getAllLibraries() {
        return libraryMapper.toLibraryDto(libraryService.findAll());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LibraryDto getById(@PathVariable Long id) {
        return libraryMapper.toLibraryDto(libraryService.getLibraryById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LibraryDto createLibrary(@RequestBody @Valid LibraryDto libraryDto) {
        return libraryMapper.toLibraryDto(libraryService.createLibrary(libraryMapper.toLibrary(libraryDto)));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LibraryDto updateLibrary(@PathVariable Long id, @RequestBody @Valid LibraryDto libraryDto) {
        return libraryMapper.toLibraryDto(libraryService.updateLibrary(id, libraryMapper.toLibrary(libraryDto)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLibrary(@PathVariable Long id) {
        libraryService.deleteLibrary(id);
    }
}