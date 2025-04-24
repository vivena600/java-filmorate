package ru.yandex.practicum.filmorate.storage;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.enums.FriendshipStatus;
import ru.yandex.practicum.filmorate.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class DbFrendShip {
    private final JdbcTemplate jdbcTemplate;

    public void addFrends(long userId, long frendId, FriendshipStatus status) {
        log.info("Пользователь {} хочет добавить {} в друзья, статус дружбы {}", userId, frendId, status);
        if (checkUserId(userId) && checkUserId(frendId)) {
           String query = "INSERT INTO frendship (user_id, frend_id, status_id) VALUES (?, ?, ?) ";
        }
    }

    public Boolean checkUserId(long userId) {
        String query = "SELECT * FROM users WHERE id = ?";
        List<User> users = jdbcTemplate.query(query, new UserRowMapper(), userId);
        if (users.size() < 1) {
            log.debug("Пользователь с id {} не был найден", userId);
            return false;
        }
        return true;
    }
}
