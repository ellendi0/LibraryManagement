package com.example.librarymanagement.service;

import com.example.librarymanagement.model.entity.Publisher;

import java.util.List;

public interface PublisherService {
    Publisher createPublisher(Publisher publisher);
    Publisher updatePublisher(Long id, Publisher updatedPublisher);
    Publisher getPublisherById(Long id);
    List<Publisher> getAllPublishers();
}