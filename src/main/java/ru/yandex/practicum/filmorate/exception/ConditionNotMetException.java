package ru.yandex.practicum.filmorate.exception;


public class ConditionNotMetException extends RuntimeException {

    public  ConditionNotMetException(String message) {
        super(message);
    }
}
