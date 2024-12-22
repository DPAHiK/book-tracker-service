package com.example.book_tracker_service.config;

import com.example.book_tracker_service.models.AuthToken;
import com.example.book_tracker_service.models.User;
import com.example.book_tracker_service.repo.AuthTokenRepository;
import com.example.book_tracker_service.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BearerTokenAuthenticationFilterTest {

    @Mock
    private AuthTokenRepository authTokenRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BearerTokenAuthenticationFilter bearerTokenAuthenticationFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private FilterChain filterChain;
    private AuthToken authToken;
    private User user;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = mock(FilterChain.class);

        user = new User();
        user.setId(1L);
        user.setName("testUser");

        authToken = new AuthToken();
        authToken.setToken("validToken");
        authToken.setUsername("testUser");
    }

    @Test
    void testDoFilterInternal_ValidToken() throws ServletException, IOException {
        request.addHeader("Authorization", "Bearer validToken");

        when(authTokenRepository.findByToken(anyString())).thenReturn(Optional.of(authToken));
        when(userRepository.findByName(anyString())).thenReturn(Optional.of(user));

        bearerTokenAuthenticationFilter.doFilterInternal(request, response, filterChain);

        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals("testUser", ((User) authentication.getPrincipal()).getUsername());

        verify(authTokenRepository, times(1)).findByToken("validToken");
        verify(userRepository, times(1)).findByName("testUser");
    }

    @Test
    void testDoFilterInternal_InvalidToken() throws ServletException, IOException {
        request.addHeader("Authorization", "Bearer invalidToken");

        when(authTokenRepository.findByToken(anyString())).thenReturn(Optional.empty());

        bearerTokenAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());

        verify(authTokenRepository, times(1)).findByToken("invalidToken");
        verify(userRepository, times(0)).findByName(anyString());
    }

    @Test
    void testDoFilterInternal_NoToken() throws ServletException, IOException {
        bearerTokenAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());

        verify(authTokenRepository, times(0)).findByToken(anyString());
        verify(userRepository, times(0)).findByName(anyString());
    }
}
