package com.example.librarymanagement.dto;

import com.example.librarymanagement.model.entity.Journal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JournalDto {
    private LocalDate dateOfBorrowing;
    private LocalDate dateOfReturning;
    private String title;
    private String authorName;
    private UserResponseDto userResponseDto;
    private String nameOfLibrary;

    public JournalDto(Journal journal) {
        this.dateOfBorrowing = journal.getDateOfBorrowing();
        this.dateOfReturning = journal.getDateOfReturning();
        this.userResponseDto = new UserResponseDto(journal.getUser());
        this.title = journal.getBookPresence().getBook().getTitle();
        this.authorName = journal.getBookPresence().getBook().getAuthor()
                .getFirstName() + " " + journal.getBookPresence().getBook().getAuthor().getLastName();
        this.nameOfLibrary = journal.getBookPresence().getBook().getTitle();
    }
}