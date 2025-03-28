package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    public User createUser(final User newUser);

    public void deleteUser(final User user);

    public User updateUser(final User userUp);

    public User getUserById(final Long userId);

    public Collection<User> getUsers();
}
