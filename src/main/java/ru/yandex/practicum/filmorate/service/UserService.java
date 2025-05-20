package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendDao;
import ru.yandex.practicum.filmorate.storage.user.UserDao;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserDao userStorage;
    private final FriendDao friendStorage;

    public User createUser(UserDto newUser) {
        return userStorage.createUser(newUser);
    }

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUserById(Long id) {
        return userStorage.getUserById(id);
    }

    public User updateUser(UserDto userUp) {
        return userStorage.updateUser(userUp);
    }

    public void addFriend(Long userId, Long friendId) {
        log.info("Пользователь {} хочет добавить в друзья{}", userId, friendId);
        userStorage.checkUserId(userId);
        userStorage.checkUserId(friendId);
        friendStorage.addFriends(userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) {
        log.info("Пользователь {} хочет удалить из друзей {}", userId, friendId);
        userStorage.checkUserId(userId);
        userStorage.checkUserId(friendId);
        friendStorage.removeFriends(userId, friendId);
    }

    public Collection<User> getFriends(Long id) {
        log.info("Запрос на получение информации о друзьях пользователя {}", id);
        userStorage.checkUserId(id);
        return friendStorage.getFriends(id);
    }

    public Collection<User> getCommonFriends(Long userId, Long friendId) {
        log.info("Запрос на получение общих друзей пользователй {} и {}", userId, friendId);
        userStorage.checkUserId(userId);
        userStorage.checkUserId(friendId);
        return friendStorage.getCommonFriends(userId, friendId);
    }
}
