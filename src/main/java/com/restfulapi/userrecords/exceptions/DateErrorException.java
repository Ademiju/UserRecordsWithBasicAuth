package com.restfulapi.userrecords.exceptions;

public class DateErrorException extends UserNotFoundException {
    public DateErrorException(String message) {
        super(message);
    }
}
