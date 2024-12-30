package com.example.book_tracker_service.controllers;

import com.example.book_tracker_service.models.BookTracker;
import com.example.book_tracker_service.response_and_request.ResponseHandler;
import com.example.book_tracker_service.services.BookTrackerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
public class BookTrackerController {

    private static final Logger logger = LoggerFactory.getLogger(BookTrackerService.class);

    final private BookTrackerService bookTrackerService;

    public BookTrackerController(BookTrackerService bookTrackerService) {
        this.bookTrackerService = bookTrackerService;
    }

    @GetMapping("/book/free")
    public ResponseEntity<?> getFreeBooks(){

        return ResponseHandler.generateResponse(HttpStatus.OK, "data", bookTrackerService.findFreeBooks());
    }

    @PostMapping("/book/tracker")
    public ResponseEntity<?> addBookTracker(@RequestBody BookTracker bookTracker){
        bookTrackerService.addBookTracker(bookTracker);

        return ResponseHandler.generateResponse(HttpStatus.OK, "message", "BookTracker added");
    }

    @DeleteMapping("/book/tracker/{id}")
    public ResponseEntity<?> deleteBookTracker(@PathVariable(value = "id") Long id){
        boolean result = bookTrackerService.deleteBookTrackerById(id);

        return ResponseHandler.generateResponse(result ? HttpStatus.OK : HttpStatus.NOT_FOUND, "deleted", result);

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

            return ResponseHandler.generateResponse( HttpStatus.OK , "deleted", "Book tracker edited");
        }

        logger.warn("While editing: bookTracker with id " + id +" not found");


        return ResponseHandler.generateResponse( HttpStatus.NOT_FOUND , "message", "Book tracker with id " + id + " not found");
    }
}
