package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import jakarta.validation.Validator;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class FilmTests {
    private FilmController controller;
    private Validator validator;
    private static Film film;

    @BeforeEach
    public void startTest() {
        film = Film.builder().
                name("film name")
                .duration(3)
                .description("description film's")
                .releaseDate(LocalDate.parse("2025-03-25"))
                .build();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void createFilm() {
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty(), "Программа увидела ошибку, которой не должно быть");
    }

    @Test
    void createFilmWithFailDuration() {
        Film newfilm = film.toBuilder()
                .duration(-3)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(newfilm);
        assertFalse(violations.isEmpty(), "Программа не увидела ошибку, связанную с отрицательной " +
                "продолжительностью фильма");
    }

    @Test
    void createFilmWithFailDescription() {
        Film newfilm = film.toBuilder()
                .description("a".repeat(201))
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(newfilm);
        assertFalse(violations.isEmpty(), "Программа не увидела ошибку, связанную с некорректным " +
                "описанием фильма");

        Film newfilm2 = film.toBuilder()
                .description("")
                .build();
        Set<ConstraintViolation<Film>> violations2 = validator.validate(newfilm2);
        assertFalse(violations2.isEmpty(), "Программа не увидела ошибку, связанную с записью пустого " +
                "описания фильма");
    }

    @Test
    void createFilmWithFailName() {
        Film newfilm = film.toBuilder()
                .name("")
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(newfilm);
        assertFalse(violations.isEmpty(), "Программа не увидела ошибку, связанную с пустым " +
                "названием фильма");
    }
}
