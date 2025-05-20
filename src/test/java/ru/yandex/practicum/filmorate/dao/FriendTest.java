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
import ru.yandex.practicum.filmorate.storage.FriendDao;
import ru.yandex.practicum.filmorate.storage.user.UserDao;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({ FriendDao.class,
        UserDao.class,
        UserMapper.class})
public class FriendTest {
    private final FriendDao friendDao;
    private static UserDto user;
    private static UserDto friend;
    private static User userInBd;
    private static User friendInBd;
    private Long i = 0L;
    @Autowired
    private UserDao userDao;

    @BeforeEach
    public void startTest() {
        user = UserDto.builder()
                .name("film name" + i)
                .email("test" + i + "@email.ru")
                .birthday(LocalDate.parse("2024-04-04"))
                .login("login" + i)
                .build();
        userInBd = userDao.createUser(user);
        i++;

        friend = UserDto.builder()
                .name("film name" + i)
                .email("test" + i + "@email.ru")
                .birthday(LocalDate.parse("2024-04-04"))
                .login("login" + i)
                .build();

        friendInBd = userDao.createUser(friend);
        i++;
    }

    @DisplayName("Отпрaвка запроса на добавление в друзья")
    @Test
    void addFriend() {
        long userId = userInBd.getId();
        long friendId = friendInBd.getId();
        friendDao.addFriends(userId, friendId);
        Collection<User> user1Friends = friendDao.getFriends(userId);
        Collection<User> user2Friends = friendDao.getFriends(friendId);
        assertEquals(user1Friends.size(), 1, "У юзера не добавился друг");
        assertEquals(user2Friends.size(), 0, "У второго пользователя не должны были появиться друзья");
    }

    @DisplayName("Удаление друзей")
    @Test
    void testDeleteFriend() {
        long userId = userInBd.getId();
        long friendId = friendInBd.getId();
        friendDao.addFriends(userId, friendId);
        Collection<User> user1Friends = friendDao.getFriends(userId);
        Collection<User> user2Friends = friendDao.getFriends(friendId);
        assertEquals(user1Friends.size(), 1, "У юзера не добавился друг");
        assertEquals(user2Friends.size(), 0, "У второго пользователя не должны были появиться друзья");

        friendDao.removeFriends(userId, friendId);
        Collection<User> user1FriendsBeforeDelete = friendDao.getFriends(userId);
        assertEquals(user1FriendsBeforeDelete.size(), 0, "друг не удалился");
    }
}
