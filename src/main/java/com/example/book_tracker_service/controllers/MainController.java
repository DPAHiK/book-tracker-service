package com.example.book_tracker_service.controllers;

import com.example.book_tracker_service.models.BookTracker;
import com.example.book_tracker_service.services.BookTrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> addBookTracker(@RequestBody BookTracker bookTracker){
        bookTrackerService.addBookTracker(bookTracker);

        return ResponseEntity.ok("BookTracker added");
    }

    @DeleteMapping("/book/tracker/{id}")
    public boolean deleteBookTracker(@PathVariable(value = "id") Long id){
        return bookTrackerService.deleteBookTrackerById(id);
    }

    @PutMapping("/book/tracker/{id}")
    public ResponseEntity<?> editBookTracker(@RequestBody BookTracker bookTracker, @PathVariable(value = "id") Long id){
        Optional<BookTracker> oldBookTracker = bookTrackerService.findById(id);
        if(oldBookTracker.isPresent()){
            BookTracker newBookTracker = oldBookTracker.get();

            newBookTracker.setFree(bookTracker.isFree());
            newBookTracker.setBookId(bookTracker.getBookId());
            newBookTracker.setReturnDate(bookTracker.getReturnDate());
            newBookTracker.setTakeDate(bookTracker.getTakeDate());

            bookTrackerService.addBookTracker(newBookTracker);

            return ResponseEntity.ok("BookTracker edited");
        }

        System.out.println("While editing: bookTracker with id " + id +" not found");


        return ResponseEntity.status(404).body("BookTracker with id \" + id +\" not found");
    }
}
