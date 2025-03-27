package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ConditionNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User createUser(User newUser) {
        log.debug("Запрос на добавление пользователя {}", newUser.toString());
        validator(newUser);
        newUser.setId(getNextId());
        users.put(newUser.getId(), newUser);
        log.debug("Пользователь зарегестрировался {}", newUser.toString());
        return newUser;
    }

    @Override
    public void deleteUser(User user) {
        //TODO наверное ещё нужно удалять все фильмы, которые добавлял удаленный юзер
        if (getUserById(user.getId()) != null) {
            users.remove(user.getId());
            log.debug("Пользователь {} был удален", user);
        }
    }

    @Override
    public User updateUser(User userUp) {
        log.debug("Запрос на изменение данных о пользователе {}", userUp.toString());
        User oldUser = getUserById(userUp.getId());
        log.trace("Пользователь чьи данные будем обнавлять {}", oldUser);
        oldUser.setName(userUp.getName());
        oldUser.setLogin(userUp.getLogin());
        oldUser.setEmail(userUp.getEmail());
        oldUser.setBirthday(userUp.getBirthday());
        oldUser.setFriends(userUp.getFriends());
        users.put(oldUser.getId(), oldUser);
        return oldUser;
    }

    @Override
    public User getUserById(Long userId) {
        if (userId == null) {
            log.trace("В запросе не бы указан id пользователя");
            throw new ConditionNotMetException("В запросе на обновление не указан id");
        }

        if (!users.containsKey(userId)) {
            log.trace("Не удалось найти пользователя по его id: {}", userId);
            throw new NotFoundException("Пользователь с id: " + userId + " не был найден");
        }
        return users.get(userId);
    }

    @Override
    public Collection<User> getUsers() {
        return List.copyOf(users.values());
    }

    private void validator(final User newUser) {
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
        currentMaxId++;
        return (long) currentMaxId;
    }
}
