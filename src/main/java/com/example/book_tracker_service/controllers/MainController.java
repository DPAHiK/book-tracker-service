package com.example.book_tracker_service.controllers;

import com.example.book_tracker_service.models.BookTracker;
import com.example.book_tracker_service.services.BookTrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class MainController {


    @Autowired
    final private BookTrackerService bookTrackerService;

    public MainController(BookTrackerService bookTrackerService) {
        this.bookTrackerService = bookTrackerService;
    }

    @GetMapping("/book/free")
    public List<BookTracker> getFreeBooks(){
        return bookTrackerService.findFreeBooks();
    }

    @PostMapping("/book/tracker")
    public void addBookTracker(@RequestBody BookTracker bookTracker){
        bookTrackerService.addBookTracker(bookTracker);
    }

    @DeleteMapping("/book/tracker/{id}")
    public void deleteBookTracker(@PathVariable(value = "id") Long id){
        bookTrackerService.deleteBookTrackerById(id);
    }

    @PostMapping("/book/tracker/{id}")
    public void editBookTracker(@RequestBody BookTracker bookTracker, @PathVariable(value = "id") Long id){
        Optional<BookTracker> oldBookTracker = bookTrackerService.findById(id);

        oldBookTracker.ifPresentOrElse(newBookTracker ->{
            newBookTracker.setFree(bookTracker.isFree());
            newBookTracker.setBookId(bookTracker.getBookId());
            newBookTracker.setReturnDate(bookTracker.getReturnDate());
            newBookTracker.setTakeDate(bookTracker.getTakeDate());

            bookTrackerService.addBookTracker(newBookTracker);
        },
        () -> {
            System.out.println("While editing: bookTracker with id " + id +" not found");
                });
    }
}
