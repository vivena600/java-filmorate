package ru.yandex.practicum.filmorate.exception;


public class ConditionNotMetException extends RuntimeException {

    public  ConditionNotMetException(final String message) {
        super(message);
    }
}
