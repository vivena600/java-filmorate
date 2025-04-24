package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    public User createUser(final UserDto newUser);

    public User updateUser(final UserDto userUp);

    public User getUserById(final Long userId);

    public Collection<User> getUsers();

    public Collection<User> getFrends(Long userId);

    public Collection<User> getCommonFrends(Long userId, Long frendId);

    public void addFrend(Long userId, Long frendId);

    public void removeFrend(Long userId, Long frendId);
}
