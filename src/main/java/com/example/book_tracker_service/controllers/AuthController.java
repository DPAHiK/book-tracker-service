package com.example.book_tracker_service.controllers;

import com.example.book_tracker_service.models.AuthToken;
import com.example.book_tracker_service.models.User;
import com.example.book_tracker_service.repo.AuthTokenRepository;
import com.example.book_tracker_service.response_and_request.LoginRequest;
import com.example.book_tracker_service.response_and_request.ResponseHandler;
import com.example.book_tracker_service.services.UserService;
import org.springframework.http.HttpStatus;
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

        if(user.isEmpty()) return ResponseHandler.generateResponse(HttpStatus.UNAUTHORIZED, "message", "Incorrect login or password");
        if(!passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword())) return ResponseHandler.generateResponse(HttpStatus.UNAUTHORIZED, "message", "Incorrect login or password");


        String token = generateToken();
        AuthToken authToken = new AuthToken();
        authToken.setToken(token);
        authToken.setUsername(loginRequest.getUsername());
        authTokenRepository.save(authToken);

        return ResponseHandler.generateResponse(HttpStatus.OK, "token", token);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody LoginRequest loginRequest){
        Optional<User> user = userService.userByName(loginRequest.getUsername());

        if(user.isPresent()) return ResponseHandler.generateResponse(HttpStatus.CONFLICT, "message", "User with name " + loginRequest.getUsername() + " already exists");
        User newUser = new User(loginRequest.getUsername(), loginRequest.getPassword(), "USER");
        userService.addUser(newUser);

        return ResponseHandler.generateResponse(HttpStatus.OK, "message", "Successful registration");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(
            value = "Authorization",
            required = false
    ) String authorizationHeader
    ) {

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseHandler.generateResponse(HttpStatus.UNAUTHORIZED, "message", "Invalid token");
        }

        Optional<AuthToken> token = authTokenRepository.findByToken(authorizationHeader.substring(7));
        token.ifPresent(authToken -> authTokenRepository.deleteById(authToken.getId()));

        return ResponseHandler.generateResponse(HttpStatus.OK, "message", "Logged out");
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }
}


