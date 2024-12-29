package com.example.book_tracker_service.controllers;

import com.example.book_tracker_service.models.BookTracker;
import com.example.book_tracker_service.services.BookTrackerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class BookTrackerControllerTest {

    @Mock
    private BookTrackerService bookTrackerService;

    @InjectMocks
    private BookTrackerController bookTrackerController;

    private MockMvc mockMvc;
    private BookTracker bookTracker;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookTrackerController).build();

        bookTracker = new BookTracker();
        bookTracker.setId(1L);
        bookTracker.setBookId(123L);
        bookTracker.setFree(true);
    }

    @Test
    void testGetFreeBooks() throws Exception {
        List<BookTracker> freeBooks = Arrays.asList(bookTracker);

        when(bookTrackerService.findFreeBooks()).thenReturn(freeBooks);

        mockMvc.perform(get("/book/free"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));

        verify(bookTrackerService, times(1)).findFreeBooks();
    }

    @Test
    void testAddBookTracker() throws Exception {
        when(bookTrackerService.addBookTracker(any(BookTracker.class))).thenReturn(bookTracker);

        mockMvc.perform(post("/book/tracker")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"bookId\":123,\"free\":true}"))
                .andExpect(status().isOk())
                .andExpect(content().string("BookTracker added"));

        verify(bookTrackerService, times(1)).addBookTracker(any(BookTracker.class));
    }

    @Test
    void testDeleteBookTracker() throws Exception {
        when(bookTrackerService.deleteBookTrackerById(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/book/tracker/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(bookTrackerService, times(1)).deleteBookTrackerById(1L);
    }

    @Test
    void testEditBookTracker() throws Exception {
        when(bookTrackerService.findById(anyLong())).thenReturn(Optional.of(bookTracker));

        mockMvc.perform(put("/book/tracker/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"bookId\":123,\"free\":false}"))
                .andExpect(status().isOk())
                .andExpect(content().string("BookTracker edited"));

        verify(bookTrackerService, times(1)).findById(1L);
        verify(bookTrackerService, times(1)).addBookTracker(any(BookTracker.class));
    }
}
