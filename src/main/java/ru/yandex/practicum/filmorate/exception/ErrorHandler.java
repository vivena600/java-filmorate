package ru.yandex.practicum.filmorate.exception;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handlerNotFound(final NotFoundException ex) {
        log.error("Не найден параметр: {}", ex.getMessage());
        return Map.of("not found", ex.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handlerValidation(final ValidationException ex) {
        log.error("Параметр не прошел проверку: {}", ex.getMessage());
        return Map.of("error validation", ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handlerServer(final RuntimeException ex) {
        log.error("Возникло не предусмотренное исключение: {}", ex.getMessage());
        return Map.of("error server", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handlerValid(final MethodArgumentNotValidException ex) {
        log.error("Параметр имеет не корректное значение: {}", ex.getMessage());
        return Map.of("error vaild", ex.getMessage());
    }

    @ExceptionHandler(ConditionNotMetException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handlerConfition(final ConditionNotMetException ex) {
        log.error("Нарушено ограниченечение поля: {}", ex.getMessage());
        return Map.of("error condition", ex.getMessage());
    }
}
