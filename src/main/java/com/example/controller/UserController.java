package com.example.controller;

import com.example.exception.DataNotFoundException;
import com.example.exception.ErrorFoundException;
import com.example.model.Address;
import com.example.model.User;
import com.example.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    private UserRepository userRepository;
    
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    // Get
    
    @GetMapping("/users/{userId}")
    public User getUser(@PathVariable Integer userId) {        
        return this.userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException(userId));
    }
    
    @GetMapping("/users/{userId}/home_address")
    public Address getUserHomeAddress(@PathVariable Integer userId) {
        return this.userRepository.findById(userId)
                .map(user -> user.getHomeAddress())
                .orElseThrow(() -> new DataNotFoundException(userId));
    }
    
    @GetMapping("/users/{userId}/billing_address")
    public Address getUserBillingAddress(@PathVariable Integer userId) {
        return this.userRepository.findById(userId)
                .map(user -> {                    
                    Address address = user.getBillingAddress();
                    return address == null ? new Address(null, null, null, null) : address;
                })
                .orElseThrow(() -> new DataNotFoundException(userId));
    }
    
    @GetMapping("/users")
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        this.userRepository.findAll().forEach(user -> list.add(user));
        return list;
    }
    
    // Post
    @PostMapping("/users")
    public User addUser(@RequestBody User user) {
        try {
            return this.userRepository.save(user);
        } catch (Exception ex) {
            throw new ErrorFoundException("User could not be saved");
        }
    }
    
    // Put
    
    @PutMapping("/users/{userId}")
    public User updateUser(@PathVariable Integer userId, @RequestBody User user) {
        if ( !this.userRepository.existsById(userId) ) {
            throw new DataNotFoundException(userId);
        }
        
        try {
            return this.userRepository.save(user);
        } catch (Exception ex) {
            throw new ErrorFoundException("User could not be updated");
        }
    }
    
    // Delete
    
    @DeleteMapping("/users/{userId}")
    public String deleteUser(@PathVariable Integer userId) {
        if ( !this.userRepository.existsById(userId) ) {
            throw new DataNotFoundException(userId);
        }
        
        this.userRepository.deleteById(userId);
        return "Deleted a user with id " + Integer.toString(userId);
    }
    
    @DeleteMapping("/users")
    public String deleteAllUsers() {
        this.userRepository.deleteAll();
        return "All users are deleted successfully";
    }
}
