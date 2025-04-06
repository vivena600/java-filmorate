package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserStorage userStorage;

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUserById(Long id) {
        return containsUser(id);
    }

    public Collection<User> getFriends(Long id) {
        log.info("Получение списка друзей пользователя c id {}", id);
        User user = containsUser(id);
        Collection<User> frends = new HashSet<>();
        if (user.getFriends() == null) {
            log.trace("У пользователя нет друзей - программа вернет пустой список");
            return frends;
        }
        frends = user.getFriends().stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
        log.debug("Успешно найдены друзья пользователя: {}", frends);
        return frends;
    }

    public Collection<User> retainFriends(Long id, Long otherId) {
        log.info("поиск общих друзей у пользователей с id {} и {}", id, otherId);
        User user = containsUser(id);
        User otherUser = containsUser(otherId);
        Set<Long> userFr = user.getFriends();
        Set<Long> otherFr = otherUser.getFriends();

        userFr.retainAll(otherFr);
        Collection<User> retFrends = new HashSet<>();
        if (userFr == null) {
            log.trace("У пользователей нет общих друзей - программа вернет пустой список");
            return retFrends;
        }
        retFrends = userFr.stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
        log.debug("Успешно найдены общие друзья пользователей: {}", retFrends);
        return retFrends;
    }

    public User createUser(User newUser) {
        return userStorage.createUser(newUser);
    }

    public User updateUser(User userUp) {
        return userStorage.updateUser(userUp);
    }

    public void addFriend(Long userId, Long friendId) {
        log.info("Запрос на добавление пользователй с id {} и {} друг к другу в друзья", userId, friendId);
        User user = containsUser(userId);
        User friend = containsUser(friendId);

        user.addFriend(friendId);
        friend.addFriend(userId);

        userStorage.updateUser(user);
        userStorage.updateUser(friend);
        log.debug("Пользователи {} и {} добавились друг к другу в друзья", user, friend);
    }

    public void deleteFriend(Long userId, Long friendId) {
        log.info("удаление пользователй с id {} и {} из друзей", userId, friendId);
        User user = containsUser(userId);
        User friend = containsUser(friendId);

        if (user.getFriends() == null || (user.getFriends().contains(friendId)
                && friend.getFriends().contains(userId))) {
            user.deleteFriend(friendId);
            userStorage.updateUser(user);
            friend.deleteFriend(userId);
            userStorage.updateUser(friend);
            log.debug("Пользователи {} и {} удалили друг друга из друзей", user, friend);
        } else {
            log.warn("Пользователи с id {} и {} нет друг у друга в друзьях", userId, friendId);
            throw new NotFoundException("У пользователя с id " + userId + " нет в друзьях друга с id " + friendId);
        }
    }

    private User containsUser(Long userId) {
        log.trace("Проверка на существовании пользователя {}", userId);
        if (userStorage.getUserById(userId) == null) {
            log.warn("Пользователь с id {} не был найден", userId);
            throw new NotFoundException("Пользователь с id " + userId + " не был найден");
        }
        return userStorage.getUserById(userId);
    }
}
