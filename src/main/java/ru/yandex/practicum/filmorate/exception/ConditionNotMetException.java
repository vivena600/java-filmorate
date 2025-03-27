package ru.yandex.practicum.filmorate.exception;


import jakarta.validation.ValidationException;

public class ConditionNotMetException extends ValidationException {

    public  ConditionNotMetException(final String message) {
        super(message);
    }
}
