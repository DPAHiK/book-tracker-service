package com.example.book_tracker_service.services;

import com.example.book_tracker_service.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.book_tracker_service.models.User;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {


    final private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> userByName(String name){
        return userRepository.findByName(name);
    }

    public Optional<User> userById(Long id){
        return userRepository.findById(id);
    }

    public List<User> allUsers(){
        return userRepository.findAll();
    }

    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void deleteUserById(Long id){
        Optional<User> user = userRepository.findById(id);

        user.ifPresentOrElse(u ->{
            userRepository.deleteById(u.getId());
        }, ()->{
            System.out.println("While deleting: user with id " + id +" have not found");
        });
    }


}
