package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class GenresDao {
    private final JdbcTemplate jdbcTemplate;

    public Collection<Genre> getGenres() {
        log.info("Запрос на получение информации о жанрах");
        String query = "SELECT * FROM genres ORDER BY id;";
        return jdbcTemplate.query(query, new GenreRowMapper());
    }

    public Genre getGenreById(int id) {
        log.info("Запрос на получение информации о жанре с id {}", id);
        String query = "SELECT * FROM genres WHERE id = ?;";
        List<Genre> genres = jdbcTemplate.query(query, new GenreRowMapper(), id);
        if (genres.isEmpty()) {
            log.debug("жанр с id {} не был найден", id);
            throw new NotFoundException("Жанр с id: " + id + " не был найден");
        }
        return genres.getFirst();
    }
}
