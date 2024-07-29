package com.example.librarymanagement.dto.mapper;

import com.example.librarymanagement.dto.JournalDto;
import com.example.librarymanagement.model.entity.Author;
import com.example.librarymanagement.model.entity.Book;
import com.example.librarymanagement.model.entity.Journal;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class JournalMapper {
    public JournalDto toJournalDto(Journal journal) {
        JournalDto journalDto = new JournalDto();
        journalDto.setId(journal.getId());
        journalDto.setDateOfBorrowing(journal.getDateOfBorrowing());
        journalDto.setDateOfReturning(journal.getDateOfReturning());
        journalDto.setTitle(getBook(journal).getTitle());
        journalDto.setAuthorName(getAuthor(journal).getFirstName() + " " + getAuthor(journal).getLastName());
        journalDto.setUser(journal.getUser().getFirstName() + " " + journal.getUser().getLastName());
        journalDto.setNameOfLibrary(journal.getBookPresence().getLibrary().getName());
        return journalDto;
    }

    public List<JournalDto> toJournalDto(List<Journal> journals) {
        if(CollectionUtils.isEmpty(journals)) return Collections.emptyList();

        return journals.stream()
                .map(this::toJournalDto)
                .toList();
    }

    private Book getBook(Journal journal) {
        return journal.getBookPresence().getBook();
    }

    private Author getAuthor(Journal journal) {
        return getBook(journal).getAuthor();
    }
}