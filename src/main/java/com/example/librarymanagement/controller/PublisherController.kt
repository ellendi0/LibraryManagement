package com.example.librarymanagement.controller;

import com.example.librarymanagement.dto.PublisherDto;
import com.example.librarymanagement.dto.mapper.PublisherMapper;
import com.example.librarymanagement.service.PublisherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/publisher")
@RequiredArgsConstructor
public class PublisherController {
    private final PublisherService publisherService;
    private final PublisherMapper publisherMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PublisherDto> getAllPublishers(){
        return publisherMapper.toPublisherDto(publisherService.getAllPublishers());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PublisherDto getPublisherById(@PathVariable Long id){
        return publisherMapper.toPublisherDto(publisherService.getPublisherById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PublisherDto createPublisher(@RequestBody @Valid PublisherDto publisherDto){
        return publisherMapper.toPublisherDto(publisherService.createPublisher(publisherMapper.toPublisher(publisherDto)));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PublisherDto updatePublisher(@PathVariable Long id, @RequestBody @Valid PublisherDto publisherDto){
        return publisherMapper.toPublisherDto(publisherService.updatePublisher(id, publisherMapper.toPublisher(publisherDto)));
    }
}