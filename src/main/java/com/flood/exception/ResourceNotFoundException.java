package com.flood.exception;

/**
 * ResourceNotFoundException.java
 * 
 * Custom runtime exception thrown when a requested resource 
 * (e.g., Rainfall record, Drainage channel, Flood Risk entry) is not found.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
    }
}
