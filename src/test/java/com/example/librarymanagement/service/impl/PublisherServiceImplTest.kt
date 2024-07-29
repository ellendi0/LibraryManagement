package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.data.TestDataFactory;
import com.example.librarymanagement.model.entity.Publisher;
import com.example.librarymanagement.repository.PublisherRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PublisherServiceImplTest {

    @InjectMocks
    private PublisherServiceImpl publisherServiceImpl;

    @Mock
    private PublisherRepository publisherRepository;

    private static Publisher publisher;

    @BeforeAll
    public static void init() {
        publisher = TestDataFactory.createPublisher();
    }

    @Test
    public void findAll() {
        when(publisherRepository.findAll()).thenReturn(List.of(publisher));

        assertEquals(1, publisherServiceImpl.getAllPublishers().size());
        verify(publisherRepository, times(1)).findAll();
    }

    @Test
    public void findById() {
        when(publisherRepository.findById(1L)).thenReturn(java.util.Optional.of(publisher));

        assertEquals(publisher, publisherServiceImpl.getPublisherById(1L));
        verify(publisherRepository, times(1)).findById(1L);
    }

    @Test
    public void create() {
        when(publisherRepository.save(publisher)).thenReturn(publisher);

        assertEquals(publisher, publisherServiceImpl.createPublisher(publisher));
        verify(publisherRepository, times(1)).save(publisher);
    }

    @Test
    public void update() {
        when(publisherRepository.findById(1L)).thenReturn(java.util.Optional.of(publisher));
        when(publisherRepository.save(publisher)).thenReturn(publisher);

        assertEquals(publisher, publisherServiceImpl.updatePublisher(1L, publisher));
        verify(publisherRepository, times(1)).findById(1L);
        verify(publisherRepository, times(1)).save(publisher);
    }
}