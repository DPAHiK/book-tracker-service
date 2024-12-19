package com.example.book_tracker_service.services;

import com.example.book_tracker_service.models.BookTracker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {
    @Autowired
    BookTrackerService bookTrackerService;

    @KafkaListener(topics = "add-book-topic", groupId = "book_group")
    public void listenAddBook(String bookId) {
        System.out.println("Received Book ID: " + bookId);

        BookTracker bookTracker = new BookTracker();
        bookTracker.setFree(true);
        bookTracker.setBookId(Long.valueOf(bookId));
        bookTrackerService.addBookTracker(bookTracker);
    }

    @KafkaListener(topics = "delete-book-topic", groupId = "book_group")
    public void listenDeleteBook(String bookId) {
        System.out.println("Received Book ID: " + bookId);

        bookTrackerService.deleteBookTrackerByBookId(Long.valueOf(bookId));
    }
}
