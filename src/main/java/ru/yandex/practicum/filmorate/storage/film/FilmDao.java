package ru.yandex.practicum.filmorate.storage.film;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exception.ConditionNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.mapper.RatingRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Reting;
import ru.yandex.practicum.filmorate.storage.GenresDao;
import ru.yandex.practicum.filmorate.storage.MpaDao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class FilmDao implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmMapper mapper;
    private final RatingRowMapper mapperRating;
    private final MpaDao dbMpa;
    private final GenresDao dbGenres;

    private static final LocalDate MINREASEDATA = LocalDate.of(1895, 12, 28);
    private static final String SQL_GET_ALL_FILMS = "SELECT f.id,\n" +
            "       f.name,\n" +
            "       f.description,\n" +
            "       f.release_date,\n" +
            "       f.duration,\n" +
            "       m.mpa_id,\n" +
            "       m.name AS name_mpa\n" +
            "FROM films AS f\n" +
            "LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id\n";

    @Override
    public Film createFilm(FilmDto newFilm) {
        log.info("Запрос на добавление фильма {}", newFilm.toString());
        validatorReleaseDate(newFilm);
        String query = "INSERT INTO films (name, description, release_date, duration, mpa_id) VALUES (?, ?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        Reting mpa = getMpa(newFilm);

        try {
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(query, new String[] {"id"});

                ps.setString(1, newFilm.getName());
                ps.setString(2, newFilm.getDescription());
                ps.setDate(3, Date.valueOf(newFilm.getReleaseDate()));
                ps.setInt(4, newFilm.getDuration());
                ps.setInt(5, mpa.getId());

                return ps;
            }, keyHolder);

            long id = keyHolder.getKey().longValue();
            newFilm.setId(id);
            newFilm.setMpa(mpa);
            if (newFilm.getGenres() != null && !newFilm.getGenres().isEmpty()) {
                setGenres(newFilm);
            }
            return mapper.mapToFilm(newFilm);
        } catch (DataAccessException ex) {
            log.error("ошибка при создании фильма {}", ex.getMessage());
            throw new RuntimeException("Не удалось создать фильм");
        }
    }

    @Override
    public void deleteFilm(FilmDto film) {
        long id = film.getId();
        log.info("Запрос на удаление фильма с id {}", id);
        String sql = "DELETE FROM films WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Film update(FilmDto filmUp) {
        log.info("Запрос на обновление данных фильма: {}", filmUp.toString());
        long id = filmUp.getId();
        chekFilmId(id);

        deleteFilm(filmUp);
        Integer mpaId = filmUp.getMpa() != null ? filmUp.getMpa().getId() : null;

        String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ?" +
                " WHERE id = ?";
        jdbcTemplate.update(sql, filmUp.getName(), filmUp.getDescription(), filmUp.getReleaseDate(),
                filmUp.getDuration(), mpaId, id);
        if (filmUp.getGenres() != null && !filmUp.getGenres().isEmpty()) {
            setGenres(filmUp);
        }
        return mapper.mapToFilm(filmUp);
    }

    @Override
    public Collection<Film> getFilms() {
        log.info("Запрос на получение информации о всех фильмах");

        //заполнение основных полей фильма
        List<Film> films = jdbcTemplate.query(SQL_GET_ALL_FILMS, new FilmRowMapper(dbMpa));
        log.debug("Получено фильмов {}", films.size());

        String genreSql = "SELECT g.id, g.name FROM films_genre AS fg LEFT JOIN films AS f ON f.id = fg.film_id " +
                "LEFT JOIN genres AS g ON fg.genre_id = g.id WHERE f.id = ?";

        for (Film film : films) {
            long id = film.getId();
            List<Genre> genre = jdbcTemplate.query(genreSql, new GenreRowMapper(), id);
            film.setGenres(new HashSet<>(genre));
        }
        return films;
    }

    @Override
    public Film getFilmById(Long filmId) {
        log.info("Запрос на получение информации о всех пользователе с id {}", filmId);
        if (filmId == null) {
            log.error("В запросе не бы указан id пользователя");
            throw new ConditionNotMetException("В запросе на обновление не указан id");
        }

        String sql = SQL_GET_ALL_FILMS + " WHERE id = ?";

        List<Film> films = jdbcTemplate.query(sql, new FilmRowMapper(dbMpa), filmId);

        if (films.size() < 1) {
            log.error("Не удалось найти пользователя по его id: {}", filmId);
            throw new NotFoundException("Пользователь с id: " + filmId + " не был найден");
        }

        Film film = films.getFirst();

        String genreSql = "SELECT g.id, g.name FROM films_genre AS fg LEFT JOIN films AS f ON f.id = fg.film_id " +
                "LEFT JOIN genres AS g ON fg.genre_id = g.id WHERE f.id = ?";
        List<Genre> genre = jdbcTemplate.query(genreSql, new GenreRowMapper(), filmId);
        film.setGenres(new HashSet<>(genre));

        return film;
    }

    private void setGenres(FilmDto film) {
        log.debug("Получение жанров фильма из запроса");
        log.trace("Жанры {}", film.getGenres());

        Set<Genre> genres = new HashSet<>();
        for (Genre genre : film.getGenres()) {
            log.debug("Проверка жанра c id {}", genre.getId());
            if ( genre.getId() != 0 && dbGenres.getGenreById(genre.getId()) != null) {
                genres.add(genre);
            }
        }

        long filmId = film.getId();
        log.trace("Запрос на добавление жанров {} фильма с id {}", genres.toString(), filmId);
        String sql = "INSERT INTO films_genre (genre_id, film_id) VALUES (?, ?)";
        List<Object[]> params = genres.stream()
                .map(genre -> new Object[] { genre.getId(), filmId })
                .collect(Collectors.toList());
        log.trace("параметры запроса {}", params.toString());

        jdbcTemplate.batchUpdate(sql, params);

        film.setGenres(genres);
    }

    private Reting getMpa(FilmDto film) {
        if (film.getMpa() == null) {
            return null;
        }
        log.trace("Полученный mpa {}", dbMpa.getRatingById(film.getMpa().getId()));
        return dbMpa.getRatingById(film.getMpa().getId());
    }

    private void validatorReleaseDate(final FilmDto newFilm) {
        log.trace("Проверяем фильм на соответствие даты релиза");
        if (newFilm.getReleaseDate().isBefore(MINREASEDATA)) {
            log.warn("Дата релиза {} раньше минимальной даты {}", newFilm, MINREASEDATA);
            throw new ConditionNotMetException("Дата релиза должна быть не раньше " + MINREASEDATA);
        }
    }

    @Override
    public Boolean chekFilmId(final long filmId) {
        String query = "SELECT * FROM films WHERE id = ?";
        List<Film> film = jdbcTemplate.query(query, new FilmRowMapper(dbMpa), filmId);
        if (film.size() < 1) {
            log.error("Не удалось найти фильм по его id: {}", filmId);
            throw new NotFoundException("Фильм с id: " + filmId + " не был найден");
        }
        return true;
    }

    @Override
    public Collection<Film> getPopularFilms(int limit) {
        log.info("Запрос на получение {} популярных фильмов", limit);
        String sql = SQL_GET_ALL_FILMS + "LEFT JOIN film_likes AS fl ON f.id = fl.film_id\n" +
                "GROUP BY f.id, f.name, f.description, f.release_date, f.duration, m.mpa_id, m.name\n" +
                "ORDER BY COUNT(fl.film_id) DESC\n" +
                "LIMIT ?";
        return jdbcTemplate.query(sql, new FilmRowMapper(dbMpa), limit);
    }
}
