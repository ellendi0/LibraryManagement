package com.example.librarymanagement.dto.mapper;

import com.example.librarymanagement.dto.JournalDto;
import com.example.librarymanagement.model.entity.Journal;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class JournalMapper {
    public static List<JournalDto> toJournalDto(List<Journal> journals) {
        List<JournalDto> journalDtos = new ArrayList<>();

        if(!journals.isEmpty()){
            journalDtos = journals.stream().map(JournalDto::new).toList();
        }
        return journalDtos;
    }
}