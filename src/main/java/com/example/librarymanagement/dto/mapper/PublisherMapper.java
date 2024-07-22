package com.example.librarymanagement.dto.mapper;

import com.example.librarymanagement.dto.PublisherDto;
import com.example.librarymanagement.model.entity.Publisher;

import java.util.ArrayList;
import java.util.List;

public class PublisherMapper {
    public static Publisher toPublisher(PublisherDto publisherDto) {
        Publisher publisher = new Publisher();
        publisher.setName(publisherDto.getName());
        return publisher;
    }

    public static List<PublisherDto> toPublishers(List<Publisher> publishers) {
        List<PublisherDto> publishersDto = new ArrayList<>();

        if(!publishers.isEmpty()){
            publishersDto = publishers.stream().map(PublisherDto::new).toList();
        }
        return publishersDto;
    }
}