package com.example.book_tracker_service.services;

import com.example.book_tracker_service.models.User;
import com.example.book_tracker_service.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MyUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MyUserDetailsService myUserDetailsService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("testUser");
        user.setPassword("password");
    }

    @Test
    void testLoadUserByUsername_UserFound() {
        when(userRepository.findByName("testUser")).thenReturn(Optional.of(user));
        User foundUser = (User) myUserDetailsService.loadUserByUsername("testUser");
        assertNotNull(foundUser);
        assertEquals("testUser", foundUser.getUsername());
        verify(userRepository, times(1)).findByName("testUser");
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByName("nonExistentUser")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> {
            myUserDetailsService.loadUserByUsername("nonExistentUser");
        });
        verify(userRepository, times(1)).findByName("nonExistentUser");
    }
}
