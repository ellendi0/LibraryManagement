package com.example.librarymanagement.dto.mapper;

import com.example.librarymanagement.dto.LibraryDto;
import com.example.librarymanagement.model.entity.Library;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LibraryMapper {
    public Library toLibrary(LibraryDto libraryDto) {
        Library library = new Library();
        library.setName(libraryDto.getName());
        library.setAddress(libraryDto.getAddress());
        return library;
    }

    public LibraryDto toLibraryDto(Library library) {
        LibraryDto libraryDto = new LibraryDto();
        libraryDto.setId(library.getId());
        libraryDto.setName(library.getName());
        libraryDto.setAddress(library.getAddress());
        return libraryDto;
    }

    public List<LibraryDto> toLibraryDto(List<Library> libraries) {
        if (libraries == null || libraries.isEmpty()) return new ArrayList<>();

        return libraries.stream()
                .map(this::toLibraryDto)
                .toList();
    }
}