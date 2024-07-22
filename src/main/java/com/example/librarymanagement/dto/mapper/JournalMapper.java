package com.example.librarymanagement.dto.mapper;

import com.example.librarymanagement.dto.JournalDto;
import com.example.librarymanagement.model.entity.Journal;

import java.util.ArrayList;
import java.util.List;

public class JournalMapper {
    public static List<JournalDto> toJournalDto(List<Journal> journals) {
        List<JournalDto> journalDtos = new ArrayList<>();

        if(!journals.isEmpty()){
            journalDtos = journals.stream().map(JournalDto::new).toList();
        }
        return journalDtos;
    }
}