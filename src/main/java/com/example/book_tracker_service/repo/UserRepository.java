package com.example.book_tracker_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.book_tracker_service.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByName(String username);
}
