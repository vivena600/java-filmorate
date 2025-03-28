package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film createFilm(final Film newFilm);

    void deleteFilm(final Film film);

    Film update(final Film filmUp);

    Collection<Film> getFilms();

    Film getFilmById(Long filmId);
}
