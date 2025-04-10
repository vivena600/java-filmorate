package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import jakarta.validation.Validator;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private FilmController controller;
    private Validator validator;
    private static User user;

    @BeforeEach
    public void startTest() {
        user = User.builder()
                .name("film name")
                .email("test@email.ru")
                .birthday(LocalDate.parse("2024-04-04"))
                .login("login")
                .build();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @DisplayName("Создание пользователя")
    @Test
    void createUser() {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "Программа увидела ошибку, которой не должно быть");
    }

    @DisplayName("Создание пользователя с некорректной датой рождения")
    @Test
    void createUserWithFailBirthday() {
        User newUser = user.toBuilder()
                .birthday(LocalDate.now())
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(newUser);
        assertFalse(violations.isEmpty(), "Программа не увидела ошибку, связанную с некорректным днём" +
                " рождения пользователя");
    }

    @DisplayName("Создание пользователя с некорректной почтой")
    @Test
    void createUserWithFailEmail() {
        User newUser = user.toBuilder()
                .email("")
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(newUser);
        assertFalse(violations.isEmpty(), "Программа не увидела ошибку, связанную с пустым email ");

        User newUser2 = user.toBuilder()
                .email("fail email")
                .build();
        Set<ConstraintViolation<User>> violations2 = validator.validate(newUser2);
        assertFalse(violations2.isEmpty(), "Программа не увидела ошибку, связанную с записью пустым " +
                "адресом электронной почтой");
    }

    @DisplayName("Создание пользователя с некорректным логином")
    @Test
    void createUserWithFailLogin() {
        User newUser = user.toBuilder()
                .login(null)
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(newUser);
        assertFalse(violations.isEmpty(), "Программа не увидела ошибку, связанную с пустым " +
                "логином пользователя");
    }
}
