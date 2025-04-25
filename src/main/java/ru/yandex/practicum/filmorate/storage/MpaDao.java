package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.RatingRowMapper;
import ru.yandex.practicum.filmorate.model.Reting;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class MpaDao {
    private final JdbcTemplate jdbcTemplate;

    public Collection<Reting> getRatings() {
        log.info("Запрос на получение информации о возрастных рейтингах");
        String query = "SELECT * FROM mpa ORDER BY mpa_id;";
        return jdbcTemplate.query(query, new RatingRowMapper());
    }

    public Reting getRatingById(int id) {
        log.info("Запрос на получение информации о рейтинге с id {}", id);
        String query = "SELECT * FROM mpa WHERE mpa_id = ?;";
        List<Reting> ratings = jdbcTemplate.query(query, new RatingRowMapper(), id);
        if (ratings.isEmpty()) {
            log.debug("рейтинг с id {} не был найден", id);
            throw new NotFoundException("Рейтинг с id: " + id + " не был найден");
        }
        return ratings.getFirst();
    }
}
