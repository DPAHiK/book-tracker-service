package com.example.book_tracker_service.services;

import com.example.book_tracker_service.models.BookTracker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConsumerServiceTest {

    @Mock
    private BookTrackerService bookTrackerService;

    @InjectMocks
    private ConsumerService consumerService;

    private BookTracker bookTracker;

    @BeforeEach
    void setUp() {
        bookTracker = new BookTracker();
        bookTracker.setId(1L);
        bookTracker.setBookId(123L);
        bookTracker.setFree(true);
    }

    @Test
    void testListenAddBook() {
        consumerService.listenAddBook("123");

        verify(bookTrackerService, times(1)).addBookTracker(any(BookTracker.class));
    }

    @Test
    void testListenDeleteBook() {
        consumerService.listenDeleteBook("123");

        verify(bookTrackerService, times(1)).deleteBookTrackerByBookId(anyLong());
    }
}
