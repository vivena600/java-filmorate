package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    User createUser(final UserDto newUser);

    User updateUser(final UserDto userUp);

    User getUserById(final Long userId);

    Collection<User> getUsers();

    Collection<User> getFrends(Long userId);

    Collection<User> getCommonFrends(Long userId, Long frendId);

    void addFrend(Long userId, Long frendId);

    void removeFrend(Long userId, Long frendId);

    Boolean checkUserId(long userId);
}
