package com.example.librarymanagement.service.impl;

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

    private static Publisher publisher1;
    private static Publisher publisher2;

    @BeforeAll
    public static void init() {
        publisher1 = new Publisher();
        publisher1.setId(1L);
        publisher1.setName("Publisher1");

        publisher2 = new Publisher();
        publisher2.setId(2L);
        publisher2.setName("Publisher2");
    }

    @Test
    public void findAll() {
        when(publisherRepository.findAll()).thenReturn(List.of(publisher1, publisher2));

        assertEquals(2, publisherServiceImpl.getAllPublishers().size());
        verify(publisherRepository, times(1)).findAll();
    }

    @Test
    public void findById() {
        when(publisherRepository.findById(1L)).thenReturn(java.util.Optional.of(publisher1));

        assertEquals(publisher1, publisherServiceImpl.getPublisherById(1L));
        verify(publisherRepository, times(1)).findById(1L);
    }

    @Test
    public void create() {
        when(publisherRepository.save(publisher1)).thenReturn(publisher1);

        assertEquals(publisher1, publisherServiceImpl.createPublisher(publisher1));
        verify(publisherRepository, times(1)).save(publisher1);
    }

    @Test
    public void update() {
        when(publisherRepository.findById(1L)).thenReturn(java.util.Optional.of(publisher1));
        when(publisherRepository.save(publisher1)).thenReturn(publisher1);

        assertEquals(publisher1, publisherServiceImpl.updatePublisher(1L, publisher1));
        verify(publisherRepository, times(1)).findById(1L);
        verify(publisherRepository, times(1)).save(publisher1);
    }
}