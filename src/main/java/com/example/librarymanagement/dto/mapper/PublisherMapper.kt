package com.example.librarymanagement.dto.mapper;

import com.example.librarymanagement.dto.PublisherDto;
import com.example.librarymanagement.model.entity.Publisher;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class PublisherMapper {
    public Publisher toPublisher(PublisherDto publisherDto) {
        Publisher publisher = new Publisher();
        publisher.setName(publisherDto.getName());
        return publisher;
    }

    public PublisherDto toPublisherDto(Publisher publisher) {
        PublisherDto publisherDto = new PublisherDto();
        publisherDto.setId(publisher.getId());
        publisherDto.setName(publisher.getName());
        return publisherDto;
    }

    public List<PublisherDto> toPublisherDto(List<Publisher> publishers) {
        if(CollectionUtils.isEmpty(publishers)) return Collections.emptyList();

        return publishers.stream()
                .map(this::toPublisherDto)
                .toList();
    }
}