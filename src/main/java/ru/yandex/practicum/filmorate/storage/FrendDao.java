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
public class FrendDao {
    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;

    public void addFrends(long userId, long frendId) {
        log.info("Пользователь {} хочет добавить в друзья{}", userId, frendId);
        userStorage.checkUserId(userId);
        userStorage.checkUserId(frendId);
        String query = "INSERT INTO frendship (user_id, frends_id) VALUES (?, ?) ";
        jdbcTemplate.update(query, userId, frendId);
    }

    public void removeFrends(long userId, long frendId) {
        log.info("Пользователь {} хочет удалить из друзей {}", userId, frendId);
        userStorage.checkUserId(userId);
        userStorage.checkUserId(frendId);

        if (checkUserInFrends(userId, frendId)) {
            String query = "DELETE FROM frendship WHERE user_id = ? AND frends_id = ?";
            jdbcTemplate.update(query, userId, frendId);
        }
    }

    public Collection<User> getFrends(long userId) {
        log.info("Запрос на получение информации о друзьях пользователя {}", userId);
        userStorage.checkUserId(userId);
        String query = "SELECT * FROM frendship AS f INNER JOIN users AS u ON u.id = f.frends_id " +
                "WHERE f.user_id = ? ORDER BY u.id";
        return jdbcTemplate.query(query, new UserRowMapper(), userId);
    }

    public Collection<User> getCommonFrends(long userId, long frendId) {
        log.info("Запрос на получение общих друзей пользователй {} и {}", userId, frendId);
        userStorage.checkUserId(userId);
        userStorage.checkUserId(frendId);
        String query = "SELECT * FROM users WHERE id IN " +
                "(SELECT f.frends_id FROM frendship AS f JOIN frendship AS f2 ON f.frends_id = f2.frends_id" +
                " WHERE f.user_id = ? AND f2.user_id = ?);";
        return jdbcTemplate.query(query, new UserRowMapper(), userId, frendId);
    }

    private Boolean checkUserInFrends(long userId, long frendId) {
        log.debug("Проверяем есть ли пользователь {} в друзьях у  {}", frendId, userId);
        String sql = "SELECT * FROM frendship WHERE user_id = ? AND frends_id = ?";
        if (jdbcTemplate.queryForList(sql, userId, frendId).size() < 1) {
            log.error("Попытка обратить к пользователям, которые не добавить друг к другу в друзья");
            throw new NotFoundException("Пользователя с id " + frendId + " нет в друзьях у " + userId);
        }
        return true;
    }
}
