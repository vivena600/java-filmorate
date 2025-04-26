package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDao;

import java.time.LocalDate;
import java.util.Collection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDao.class,
        UserMapper.class})
public class UserTest {
    private final UserDao userDao;
    private static UserDto user;
    private Long i = 0L;

    @BeforeEach
    public void startTest() {
        user = UserDto.builder()
                .name("user name" + i)
                .email("testForUser" + i + "@email.ru")
                .birthday(LocalDate.parse("2024-04-04"))
                .login("login")
                .build();
        i++;
    }

    @DisplayName("Создание пользователя")
    @Test
    void createUser() {
        User createdUser = userDao.createUser(user);
        Collection<User> allUsers = userDao.getUsers();
        assertEquals(allUsers.size(), i);
        assertThat(createdUser).hasFieldOrPropertyWithValue("id", createdUser.getId());
        assertThat(createdUser).hasFieldOrPropertyWithValue("name", user.getName());
        assertThat(createdUser).hasFieldOrPropertyWithValue("email", user.getEmail());
        assertThat(createdUser).hasFieldOrPropertyWithValue("birthday", user.getBirthday());
        assertThat(createdUser).hasFieldOrPropertyWithValue("login", user.getLogin());
    }

    @DisplayName("Получение пользователя по id")
    @Test
    void testGetUserById() {
        User createdUser = userDao.createUser(user);
        User foundUser = userDao.getUserById(createdUser.getId());
        assertThat(foundUser).hasFieldOrPropertyWithValue("id", i);
        assertThat(foundUser).hasFieldOrPropertyWithValue("name", user.getName());
        assertThat(foundUser).hasFieldOrPropertyWithValue("email", user.getEmail());
        assertThat(foundUser).hasFieldOrPropertyWithValue("birthday", user.getBirthday());
        assertThat(foundUser).hasFieldOrPropertyWithValue("login", user.getLogin());
    }
}
