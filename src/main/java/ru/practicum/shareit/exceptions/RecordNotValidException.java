package ru.practicum.shareit.exceptions;

public class RecordNotValidException extends RuntimeException {
    public RecordNotValidException(String message) {
        super(message);
    }
}
