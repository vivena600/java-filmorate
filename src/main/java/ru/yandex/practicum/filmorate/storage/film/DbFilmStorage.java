package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public class DbFilmStorage implements FilmStorage {
    @Override
    public Film createFilm(Film newFilm) {
        return null;
    }

    @Override
    public void deleteFilm(Film film) {

    }

    @Override
    public Film update(Film filmUp) {
        return null;
    }

    @Override
    public Collection<Film> getFilms() {
        return List.of();
    }

    @Override
    public Film getFilmById(Long filmId) {
        return null;
    }
}
