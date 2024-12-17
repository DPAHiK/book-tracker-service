package com.example.book_tracker_service.repo;

import com.example.book_tracker_service.models.BookTracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookTrackerRepository extends JpaRepository<BookTracker, Long> {
    List<BookTracker> findByIsFree(boolean isFree);
}
