package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.exception.EntityNotFoundException;
import com.example.librarymanagement.model.entity.Publisher;
import com.example.librarymanagement.repository.PublisherRepository;
import com.example.librarymanagement.service.PublisherService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublisherServiceImpl implements PublisherService {
    private final PublisherRepository publisherRepository;

    public PublisherServiceImpl(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    @Override
    public Publisher createPublisher(Publisher publisher) {
        return publisherRepository.save(publisher);
    }

    @Override
    public Publisher updatePublisher(Long id, Publisher updatedPublisher) {
        Publisher publisher = getPublisherById(id);
        publisher.setName(updatedPublisher.getName());
        return publisherRepository.save(publisher);
    }

    @Override
    public Publisher getPublisherById(Long id) {
        return publisherRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Publisher"));
    }

    @Override
    public List<Publisher> getAllPublishers() {
        return publisherRepository.findAll();
    }
}