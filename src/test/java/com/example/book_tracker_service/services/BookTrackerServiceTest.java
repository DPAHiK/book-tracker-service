package com.example.book_tracker_service.services;

import com.example.book_tracker_service.models.BookTracker;
import com.example.book_tracker_service.repo.BookTrackerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookTrackerServiceTest {

    @Mock
    private BookTrackerRepository bookTrackerRepository;

    @InjectMocks
    private BookTrackerService bookTrackerService;

    private BookTracker bookTracker;

    @BeforeEach
    void setUp() {
        bookTracker = new BookTracker();
        bookTracker.setId(1L);
        bookTracker.setBookId(123L);
        bookTracker.setFree(true);
    }

    @Test
    void testFindById() {
        when(bookTrackerRepository.findById(1L)).thenReturn(Optional.of(bookTracker));
        Optional<BookTracker> foundBookTracker = bookTrackerService.findById(1L);
        assertTrue(foundBookTracker.isPresent());
        assertEquals(1L, foundBookTracker.get().getId());
        verify(bookTrackerRepository, times(1)).findById(1L);
    }

    @Test
    void testFindFreeBooks() {
        List<BookTracker> freeBooks = Arrays.asList(bookTracker);
        when(bookTrackerRepository.findByIsFree(true)).thenReturn(freeBooks);
        List<BookTracker> foundFreeBooks = bookTrackerService.findFreeBooks();
        assertFalse(foundFreeBooks.isEmpty());
        assertEquals(1, foundFreeBooks.size());
        verify(bookTrackerRepository, times(1)).findByIsFree(true);
    }

    @Test
    void testAddBookTracker() {
        when(bookTrackerRepository.save(bookTracker)).thenReturn(bookTracker);
        BookTracker savedBookTracker = bookTrackerService.addBookTracker(bookTracker);
        assertNotNull(savedBookTracker);
        assertEquals(123L, savedBookTracker.getBookId());
        verify(bookTrackerRepository, times(1)).save(bookTracker);
    }

    @Test
    void testDeleteBookTrackerById_Success() {
        when(bookTrackerRepository.findById(1L)).thenReturn(Optional.of(bookTracker));
        boolean isDeleted = bookTrackerService.deleteBookTrackerById(1L);
        assertTrue(isDeleted);
        verify(bookTrackerRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteBookTrackerById_NotFound() {
        when(bookTrackerRepository.findById(2L)).thenReturn(Optional.empty());
        boolean isDeleted = bookTrackerService.deleteBookTrackerById(2L);
        assertFalse(isDeleted);
        verify(bookTrackerRepository, times(1)).findById(2L);
        verify(bookTrackerRepository, times(0)).deleteById(2L);
    }

    @Test
    void testDeleteBookTrackerByBookId_Success() {
        when(bookTrackerRepository.findByBookId(123L)).thenReturn(Optional.of(bookTracker));
        bookTrackerService.deleteBookTrackerByBookId(123L);
        verify(bookTrackerRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteBookTrackerByBookId_NotFound() {
        when(bookTrackerRepository.findByBookId(124L)).thenReturn(Optional.empty());
        bookTrackerService.deleteBookTrackerByBookId(124L);
        verify(bookTrackerRepository, times(1)).findByBookId(124L);
        verify(bookTrackerRepository, times(0)).deleteById(anyLong());
    }
}
