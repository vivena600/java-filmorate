package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
    }

    @PostMapping
    public User postUser(@Valid @RequestBody User newUser) {
        log.debug("Запрос на добавление пользователя {}", newUser.toString());
        validator(newUser);
        newUser.setId(getNextId());
        users.put(newUser.getId(), newUser);
        log.debug("Пользователь зарегестрировался {}", newUser.toString());
        return newUser;
    }

    @PutMapping
    public User putUser(@Valid @RequestBody User newUser) {
        log.debug("Запрос на изменение данных о пользователе {}", newUser.toString());
        if (newUser.getId() == null) {
            log.trace("В запросе не бы указан id пользователя");
            throw new ConditionNotMetException("В запросе на обновление не указан id");
        }

        if (users.containsKey(newUser.getId())) {
            validator(newUser);
            User oldUser = users.get(newUser.getId());
            log.trace("Пользователь чьи данные будем обнавлять {}", oldUser);
            oldUser.setName(newUser.getName());
            oldUser.setLogin(newUser.getLogin());
            oldUser.setEmail(newUser.getEmail());
            oldUser.setBirthday(newUser.getBirthday());
            return oldUser;
        }
        log.debug("Пользователь с id {} не был найден. Не удалось осуществить запрос: {}", newUser.getId(), newUser.toString());
        throw new NotFoundException("Пользователь с id " + newUser.getId() + " не был найден");
    }

    private void validator(User newUser) {
        if (newUser.getLogin().contains(" ")) {
            log.trace("Неправильный логин пользователя {}", newUser.getLogin());
            throw new ConditionNotMetException("Логин пользователя не может содержать пробелы");
        }
        if (newUser.getName() == null) {
            log.trace("В запросе не было имени пользователя, поэтому запишем логин");
            newUser.setName(newUser.getLogin());
        }
    }

    private Long getNextId() {
        int currentMaxId = (int) users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        currentMaxId ++;
        return (long) currentMaxId;
    }
}
