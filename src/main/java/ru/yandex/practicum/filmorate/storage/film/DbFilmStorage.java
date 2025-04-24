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
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.RatingRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Reting;
import ru.yandex.practicum.filmorate.storage.DbGeneres;
import ru.yandex.practicum.filmorate.storage.mpa.DbRetingStorage;


import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class DbFilmStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmMapper mapper;
    private final RatingRowMapper mapperRating;
    private final DbRetingStorage dbMpa;
    private final DbGeneres dbGenres;

    private static final LocalDate MINREASEDATA = LocalDate.of(1895, 12, 28);

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
            if (newFilm.getGenres() == null && newFilm.getGenres().isEmpty())
            setGenres(newFilm);
            return mapper.mapToFilm(newFilm);
        } catch (DataAccessException ex) {
            log.error("ошибка при создании фильма {}", ex.getMessage());
            throw new RuntimeException("Не удалось создать фильм");
        }
    }

    @Override
    public void deleteFilm(FilmDto film) {

    }

    @Override
    public Film update(FilmDto filmUp) {
        return null;
    }

    @Override
    public Collection<Film> getFilms() {
        //select from films join MPA + select genres + select from film_genres
        // + Объединить
        return List.of();
    }

    @Override
    public Film getFilmById(Long filmId) {
        return null;
    }

    private void setGenres(FilmDto film) {
        log.debug("Получение жанров фильма из запроса");

        Set<Genre> genres = new HashSet<>();
        for (Genre genre : film.getGenres()) {
            if (dbGenres.getGenreById(genre.getGenere_id()) != null) {
                genres.add(genre);
            }
        }

        long filmId = film.getId();
        log.info("Запрос на добаление жанров {} фильма с id {}", genres.toString(), filmId);
        String sql = "INSERT INTO films_genre (genre_id, film_id) VALUES (?, ?)";
        List<Object[]> params = genres.stream()
                .map(genre -> new Object[] { genre.getGenere_id(), filmId })
                .collect(Collectors.toList());

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
        log.trace("Проверяем фильм на соответсвие даты релиза");
        if (newFilm.getReleaseDate().isBefore(MINREASEDATA)) {
            log.warn("Дата релиза {} раньше миниальной даты {}", newFilm, MINREASEDATA);
            throw new ConditionNotMetException("Дата релиза должна быть не реньше " + MINREASEDATA);
        }
    }
}
