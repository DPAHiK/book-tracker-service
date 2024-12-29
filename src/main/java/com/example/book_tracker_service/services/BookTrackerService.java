package com.example.book_tracker_service.services;

import com.example.book_tracker_service.models.BookTracker;
import com.example.book_tracker_service.repo.BookTrackerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookTrackerService {


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

        System.out.println("While deleting: bookTracker with bookId " + id +" not found");
        return false;
    }

    public void deleteBookTrackerByBookId(Long bookId){
        Optional<BookTracker> book = bookTrackerRepository.findByBookId(bookId);

        if(book.isPresent()){
            bookTrackerRepository.deleteById(book.get().getId());
            return;
        }

        System.out.println("While deleting: bookTracker with id " + bookId +" not found");

    }
}
