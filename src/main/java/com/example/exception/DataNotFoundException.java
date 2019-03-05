package com.example.exception;


public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException(Integer id) {
        super("Could not found User id " + id);
    }
    
    public DataNotFoundException(String name) {
        super("Could not found User name " + name);
    }
}
