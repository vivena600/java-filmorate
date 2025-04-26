package ru.yandex.practicum.filmorate.storage;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class FriendDao {
    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;

    public void addFriends(long userId, long friendId) {
        log.info("Пользователь {} хочет добавить в друзья{}", userId, friendId);
        userStorage.checkUserId(userId);
        userStorage.checkUserId(friendId);
        String query = "INSERT INTO frendship (user_id, frends_id) VALUES (?, ?) ";
        jdbcTemplate.update(query, userId, friendId);
    }

    public void removeFriends(long userId, long friendId) {
        log.info("Пользователь {} хочет удалить из друзей {}", userId, friendId);
        userStorage.checkUserId(userId);
        userStorage.checkUserId(friendId);
        String query = "DELETE FROM frendship WHERE user_id = ? AND frends_id = ?";
        jdbcTemplate.update(query, userId, friendId);

        /* логично было бы проверять есть ли у пользователя этот друг, прежде чем отправлять запрос на удаление,
         но Postman так ругается
        if (checkUserInFrends(userId, frendId)) {
            String query = "DELETE FROM frendship WHERE user_id = ? AND frends_id = ?";
            jdbcTemplate.update(query, userId, frendId);
        }
         */
    }

    public Collection<User> getFriends(long userId) {
        log.info("Запрос на получение информации о друзьях пользователя {}", userId);
        userStorage.checkUserId(userId);
        String query = "SELECT * FROM frendship AS f INNER JOIN users AS u ON u.id = f.frends_id " +
                "WHERE f.user_id = ? ORDER BY u.id";
        return jdbcTemplate.query(query, new UserRowMapper(), userId);
    }

    public Collection<User> getCommonFriends(long userId, long friendId) {
        log.info("Запрос на получение общих друзей пользователй {} и {}", userId, friendId);
        userStorage.checkUserId(userId);
        userStorage.checkUserId(friendId);
        String query = "SELECT * FROM users WHERE id IN " +
                "(SELECT f.frends_id FROM frendship AS f JOIN frendship AS f2 ON f.frends_id = f2.frends_id" +
                " WHERE f.user_id = ? AND f2.user_id = ?);";
        return jdbcTemplate.query(query, new UserRowMapper(), userId, friendId);
    }

    private Boolean checkUserInFriends(long userId, long friendId) {
        log.debug("Проверяем есть ли пользователь {} в друзьях у  {}", friendId, userId);
        String sql = "SELECT * FROM frendship WHERE user_id = ? AND frends_id = ?";
        if (jdbcTemplate.queryForList(sql, userId, friendId).size() < 1) {
            log.error("Попытка обратить к пользователям, которые не добавить друг к другу в друзья");
            throw new NotFoundException("Пользователя с id " + friendId + " нет в друзьях у " + userId);
        }
        return true;
    }
}
