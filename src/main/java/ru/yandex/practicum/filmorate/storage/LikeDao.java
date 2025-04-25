package ru.yandex.practicum.filmorate.storage;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class LikeDao {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final JdbcTemplate jdbcTemplate;

    public void addLike(long filmId, long userId) {
        log.info("Запрос пользователя с id: {} поставить лайк фильму с id {}", userId, filmId);
        filmStorage.chekFilmId(filmId);
        userStorage.checkUserId(userId);

        String sql = "INSERT INTO film_likes (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    public void removeLike(long filmId, long userId) {
        log.info("Запрос пользователя с id: {} удалить лайк фильму с id {}", userId, filmId);
        filmStorage.chekFilmId(filmId);
        userStorage.checkUserId(userId);

        String sql = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }
}
