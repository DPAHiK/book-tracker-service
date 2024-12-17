package com.example.book_tracker_service.services;

import com.example.book_tracker_service.BookTrackerServiceApplication;
import com.example.book_tracker_service.models.BookTracker;
import com.example.book_tracker_service.repo.BookTrackerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookTrackerService {
    @Autowired
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

    public void addBookTracker(BookTracker bookTracker){
        bookTrackerRepository.save(bookTracker);
    }


    public void deleteBookTrackerById(Long id){
        Optional<BookTracker> book = bookTrackerRepository.findById(id);

        book.ifPresentOrElse(b ->{
            bookTrackerRepository.deleteById(b.getId());
        }, ()->{
            System.out.println("While deleting: bookTracker with id " + id +" not found");
        });
    }
}
