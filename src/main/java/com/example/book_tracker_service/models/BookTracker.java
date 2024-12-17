package com.example.book_tracker_service.models;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
public class BookTracker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long bookId;
    private boolean isFree;
    private Date takeDate;
    private Date returnDate;

    public BookTracker() {
    }

    public BookTracker(Long id, Long bookId, boolean isFree, Date takeDate, Date returnDate) {
        this.id = id;
        this.bookId = bookId;
        this.isFree = isFree;
        this.takeDate = takeDate;
        this.returnDate = returnDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public Date getTakeDate() {
        return takeDate;
    }

    public void setTakeDate(Date takeDate) {
        this.takeDate = takeDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
}
