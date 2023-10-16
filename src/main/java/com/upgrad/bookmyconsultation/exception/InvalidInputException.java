package com.upgrad.bookmyconsultation.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
public class InvalidInputException extends Exception{
    private List<String> attributeNames;

    // New constructor that accepts a single String argument
    public InvalidInputException(String message) {
        super(message);
        this.attributeNames = Arrays.asList(message);
    }
}
