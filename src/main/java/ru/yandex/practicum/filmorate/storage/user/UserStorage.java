package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    User createUser(final UserDto newUser);

    User updateUser(final UserDto userUp);

    User getUserById(final Long userId);

    Collection<User> getUsers();

    Boolean checkUserId(long userId);
}
