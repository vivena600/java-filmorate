package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Reting;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenresDao;
import ru.yandex.practicum.filmorate.storage.MpaDao;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final MpaDao mpaDao;
    private final GenresDao genresDao;

    public Film addFilm(FilmDto newFilm) {
        if (newFilm.getMpa() != null) {
            Reting mpa = mpaDao.getRatingById(newFilm.getMpa().getId());
            newFilm.setMpa(mpa);
        }
        if (newFilm.getGenres() != null && !newFilm.getGenres().isEmpty()) {
            Set<Genre> genres = genresDao.checkListGenres(newFilm.getGenres());
            if (genres.size() > 0) {
                newFilm.setGenres(genres);
            }
        }
        return filmStorage.createFilm(newFilm);
    }

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilmById(Long id) {
        return filmStorage.getFilmById(id);
    }

    public Film updateFilm(FilmDto filmUp) {
        if (filmUp.getMpa() != null) {
            Reting mpa = mpaDao.getRatingById(filmUp.getMpa().getId());
            filmUp.setMpa(mpa);
        }
        if (filmUp.getGenres() != null && !filmUp.getGenres().isEmpty()) {
            filmUp.setGenres(genresDao.checkListGenres(filmUp.getGenres()));
        }
        return filmStorage.update(filmUp);
    }

    public Collection<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }
}
