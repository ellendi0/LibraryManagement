package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.data.TestDataFactory;
import com.example.librarymanagement.model.entity.Library;
import com.example.librarymanagement.repository.LibraryRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LibraryServiceImplTest {

    @InjectMocks
    private LibraryServiceImpl libraryServiceImpl;

    @Mock
    private LibraryRepository libraryRepository;
    @Mock
    private BookPresenceServiceImpl bookPresenceServiceImpl;
    @Mock
    private ReservationServiceImpl reservationService;

    private static Library library1;

    @BeforeAll
    public static void init() {
        TestDataFactory.TestDataRel testDataRel = TestDataFactory.createTestDataRel();

        library1 = testDataRel.library;
    }

    @Test
    public void testFindAll() {
        when(libraryRepository.findAll()).thenReturn(List.of(library1));

        assertEquals(List.of(library1), libraryServiceImpl.findAll());
        verify(libraryRepository, times(1)).findAll();
    }

    @Test
    public void testGetLibraryById() {
        when(libraryRepository.findById(anyLong())).thenReturn(Optional.of(library1));

        assertEquals(library1, libraryServiceImpl.getLibraryById(1L));
        verify(libraryRepository, times(1)).findById(1L);
    }

    @Test
    public void testCreateLibrary() {
        when(libraryRepository.save(library1)).thenReturn(library1);

        assertEquals(library1, libraryServiceImpl.createLibrary(library1));
        verify(libraryRepository, times(1)).save(library1);
    }

    @Test
    public void testUpdateLibrary() {
        when(libraryRepository.findById(anyLong())).thenReturn(Optional.of(library1));
        when(libraryRepository.save(library1)).thenReturn(library1);

        assertEquals(library1, libraryServiceImpl.updateLibrary(1L, library1));
        verify(libraryRepository, times(1)).findById(1L);
        verify(libraryRepository, times(1)).save(library1);
    }

    @Test
    public void deleteLibrary() {
        when(libraryRepository.findById(1L)).thenReturn(Optional.of(library1));
        doNothing().when(libraryRepository).deleteById(1L);

        libraryServiceImpl.deleteLibrary(1L);
        verify(libraryRepository, times(1)).deleteById(1L);
    }
}