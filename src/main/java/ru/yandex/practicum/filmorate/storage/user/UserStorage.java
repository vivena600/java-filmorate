package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    public User createUser(final UserDto newUser);

    public void deleteUser(final UserDto user);

    public User updateUser(final UserDto userUp);

    public User getUserById(final Long userId);

    public Collection<User> getUsers();
}
