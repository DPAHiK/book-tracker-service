package com.example.book_tracker_service.services;

import com.example.book_tracker_service.models.BookTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerService.class);

    final private BookTrackerService bookTrackerService;

    public ConsumerService(BookTrackerService bookTrackerService) {
        this.bookTrackerService = bookTrackerService;
    }

    @KafkaListener(topics = "add-book-topic", groupId = "book_group")
    public void listenAddBook(String bookId) {
        logger.info("Received Book ID (add): " + bookId);

        BookTracker bookTracker = new BookTracker();
        bookTracker.setFree(true);
        bookTracker.setBookId(Long.valueOf(bookId));
        bookTrackerService.addBookTracker(bookTracker);
    }

    @KafkaListener(topics = "delete-book-topic", groupId = "book_group")
    public void listenDeleteBook(String bookId) {
        logger.info("Received Book ID (delete): " + bookId);

       bookTrackerService.deleteBookTrackerByBookId(Long.valueOf(bookId));

    }
}
