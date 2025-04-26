package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Repository
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

    public Set<Genre> checkListGenres(Set<Genre> genres) {
        Set<Integer> genreIds = genres.stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());

        log.info("Жанры в запросе : {}", genreIds.toString());
        String sql = "SELECT id, name FROM genres WHERE id IN (" +
                String.join(",", Collections.nCopies(genreIds.size(), "?")) +
                ")";

        Set<Genre> existingGenres = new LinkedHashSet<>(
                jdbcTemplate.query(sql, genreIds.toArray(), new GenreRowMapper())
        );

        return existingGenres;
    }

    public void setGenres(FilmDto film) {
        Collection<Genre> genres = getGenres();
        log.debug("Получение жанров фильма из запроса");
        log.trace("Жанры {}", film.getGenres());

        Set<Genre> uniqueGenres = new HashSet<>(film.getGenres().stream()
                .filter(genre -> genre.getId() != 0 && getGenreById(genre.getId()) != null)
                .collect(Collectors.toMap(
                        Genre::getId,
                        genre -> genre,
                        (existing, replacement) -> existing
                ))
                .values()).stream()
                .sorted(Comparator.comparing(Genre::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        long filmId = film.getId();
        log.trace("Уникальные жанры для добавления: {}", uniqueGenres);

        String sql = "INSERT INTO films_genre (genre_id, film_id) VALUES (?, ?)";
        List<Object[]> params = uniqueGenres.stream()
                .map(genre -> new Object[] { genre.getId(), filmId })
                .collect(Collectors.toList());
        log.trace("параметры запроса {}", params.toString());
        jdbcTemplate.batchUpdate(sql, params);
        film.setGenres(uniqueGenres);
    }
}
