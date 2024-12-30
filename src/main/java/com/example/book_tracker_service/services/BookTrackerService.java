package com.example.book_tracker_service.services;

import com.example.book_tracker_service.models.BookTracker;
import com.example.book_tracker_service.repo.BookTrackerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookTrackerService {

    private static final Logger logger = LoggerFactory.getLogger(BookTrackerService.class);

    private final BookTrackerRepository bookTrackerRepository;

    public BookTrackerService(BookTrackerRepository bookTrackerRepository){
        this.bookTrackerRepository = bookTrackerRepository;
    }

    public Optional<BookTracker> findById(Long id){
        return bookTrackerRepository.findById(id);
    }

    public List<BookTracker> findFreeBooks(){
        return bookTrackerRepository.findByIsFree(true);
    }

    public BookTracker addBookTracker(BookTracker bookTracker){
        return bookTrackerRepository.save(bookTracker);
    }


    public boolean deleteBookTrackerById(Long id){
        Optional<BookTracker> book = bookTrackerRepository.findById(id);

        if(book.isPresent()){
            bookTrackerRepository.deleteById(id);
            return true;
        }

        logger.warn("While deleting: bookTracker with bookId {} not found", id);
        return false;
    }

    public void deleteBookTrackerByBookId(Long bookId){
        Optional<BookTracker> book = bookTrackerRepository.findByBookId(bookId);

        if(book.isPresent()){
            bookTrackerRepository.deleteById(book.get().getId());
            return;
        }

        logger.warn("While deleting: bookTracker with id {} not found", bookId);

    }
}
