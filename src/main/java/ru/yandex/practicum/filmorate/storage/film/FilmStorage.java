package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film createFilm(final FilmDto newFilm);

    void deleteFilm(final FilmDto film);

    Film update(final FilmDto filmUp);

    Collection<Film> getFilms();

    Film getFilmById(Long filmId);
}
