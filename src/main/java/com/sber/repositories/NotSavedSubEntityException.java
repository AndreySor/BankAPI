package com.sber.repositories;

public class NotSavedSubEntityException extends RuntimeException {
    public NotSavedSubEntityException(String message, Long id) {
        super(message +  " " + id);
    }
}
