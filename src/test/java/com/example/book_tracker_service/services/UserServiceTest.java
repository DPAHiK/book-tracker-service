package com.example.book_tracker_service.services;

import com.example.book_tracker_service.models.User;
import com.example.book_tracker_service.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("testUser");
        user.setPassword("password");
    }

    @Test
    void testUserByName() {
        when(userRepository.findByName("testUser")).thenReturn(Optional.of(user));
        Optional<User> foundUser = userService.userByName("testUser");
        assertTrue(foundUser.isPresent());
        assertEquals("testUser", foundUser.get().getUsername());
        verify(userRepository, times(1)).findByName("testUser");
    }

    @Test
    void testUserByNameNotFound() {
        Optional<User> user = userService.userByName("none");
        assertFalse(user.isPresent());
    }

    @Test
    void testUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Optional<User> foundUser = userService.userById(1L);
        assertTrue(foundUser.isPresent());
        assertEquals(1L, foundUser.get().getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testUserByIdNotFound() {
        Optional<User> user = userService.userById(2L);
        assertFalse(user.isPresent());
    }

    @Test
    void testAllUsers() {
        List<User> users = Arrays.asList(user);
        when(userRepository.findAll()).thenReturn(users);
        List<User> allUsers = userService.allUsers();
        assertFalse(allUsers.isEmpty());
        assertEquals(1, allUsers.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testAddUser() {
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        userService.addUser(user);
        assertEquals("encodedPassword", user.getPassword());
        verify(userRepository, times(1)).save(user);
        verify(passwordEncoder, times(1)).encode("password");
    }

    @Test
    void testDeleteUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        userService.deleteUserById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }




}
