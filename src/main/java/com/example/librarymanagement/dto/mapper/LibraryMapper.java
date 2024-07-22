package com.example.librarymanagement.dto.mapper;

import com.example.librarymanagement.dto.LibraryDto;
import com.example.librarymanagement.model.entity.Library;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LibraryMapper {
    public static Library toLibrary(LibraryDto libraryDto) {
        Library library = new Library();
        library.setName(libraryDto.getName());
        library.setAddress(libraryDto.getAddress());
        return library;
    }

    public static List<LibraryDto> toLibraryDto(List<Library> libraries) {
        List<LibraryDto> libraryDtos = new ArrayList<>();

        if(!libraries.isEmpty()){
            libraryDtos = libraries.stream().map(LibraryDto::new).toList();
        }
        return libraryDtos;
    }
}