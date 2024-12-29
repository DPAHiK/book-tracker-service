package com.example.book_tracker_service.config;

import com.example.book_tracker_service.models.AuthToken;
import com.example.book_tracker_service.models.User;
import com.example.book_tracker_service.repo.AuthTokenRepository;
import com.example.book_tracker_service.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class BearerTokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private final AuthTokenRepository authTokenRepository;

    @Autowired
    private final  UserRepository userRepository;

    public BearerTokenAuthenticationFilter(AuthTokenRepository authTokenRepository, UserRepository userRepository) {

        this.authTokenRepository = authTokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String token = getTokenFromRequest(request);

        if (token != null) {
            Optional<AuthToken> authToken = authTokenRepository.findByToken(token);
            if (authToken.isPresent()) {

                Optional <User> userDetails = userRepository.findByName(authToken.get().getUsername());
                if(userDetails.isPresent()){
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails.get(), null, userDetails.get().getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                else throw new UsernameNotFoundException("User with name " + authToken.get().getUsername() + " not found");

            }
        }

        chain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Удаляем "Bearer "
        }
        return null;
    }
}
