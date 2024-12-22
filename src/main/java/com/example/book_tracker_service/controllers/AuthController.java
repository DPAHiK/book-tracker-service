package com.example.book_tracker_service.controllers;

import com.example.book_tracker_service.models.AuthToken;
import com.example.book_tracker_service.models.User;
import com.example.book_tracker_service.repo.AuthTokenRepository;
import com.example.book_tracker_service.response.AuthResponse;
import com.example.book_tracker_service.response.LoginRequest;
import com.example.book_tracker_service.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    final private AuthTokenRepository authTokenRepository;

    final private UserService userService;

    final private PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder, AuthTokenRepository authTokenRepository) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authTokenRepository = authTokenRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        Optional<User> user = userService.userByName(loginRequest.getUsername());

        if(user.isEmpty()) return ResponseEntity.status(404).body("Incorrect login or password");
        if(!passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword())) return ResponseEntity.status(404).body("Incorrect login or password");


        String token = generateToken();
        AuthToken authToken = new AuthToken();
        authToken.setToken(token);
        authToken.setUsername(loginRequest.getUsername());
        authTokenRepository.save(authToken);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody LoginRequest loginRequest){
        Optional<User> user = userService.userByName(loginRequest.getUsername());
        if(user.isPresent()) return ResponseEntity.status(404).body("User with name " + loginRequest.getUsername() + " already exists");
        User newUser = new User(loginRequest.getUsername(), loginRequest.getPassword(), "USER");
        userService.addUser(newUser);
        return ResponseEntity.ok("Successful registration");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(
            value = "Authorization",
            required = false
    ) String authorizationHeader
    ) {

        if (
                authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")
        ) {
            return ResponseEntity.status(401).build();
        }

        Optional<AuthToken> token = authTokenRepository.findByToken(authorizationHeader.substring(7));

        token.ifPresent(authToken -> authTokenRepository.deleteById(authToken.getId()));

        return ResponseEntity.status(200).body("Logged out");
    }

    private String generateToken() {

        return UUID.randomUUID().toString();
    }
}
