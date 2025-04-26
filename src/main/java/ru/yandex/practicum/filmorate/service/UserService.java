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
        friendStorage.addFriends(userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) {
        friendStorage.removeFriends(userId, friendId);
    }

    public Collection<User> getFriends(Long id) {
        return friendStorage.getFriends(id);
    }

    public Collection<User> getCommonFriends(Long userId, Long friendId) {
        return friendStorage.getCommonFriends(userId, friendId);
    }
}
