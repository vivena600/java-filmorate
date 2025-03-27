package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Collection<Film> getPopularFilms(int count) {
        log.info("Запрос на получение {} популярных фильмов", count);
        int newCount = count;
        if (newCount < 0) {
            throw new ConditionNotMetException("Количество элементов не может быть отрицательным числом");
        }
        Collection<Film> sortFilmList = sortFilms();
        log.info("количество фильмов в базе: {} количество count: {}", sortFilmList.size(), count);
        if (count > sortFilmList.size()) {
            newCount = sortFilmList.size();
        }
        log.trace("Введеное count больше количества фильмов, count изменен на {}", newCount);
        log.trace("Отсортированные списки : {}", sortFilmList);
        return sortFilmList.stream().toList().subList(0, newCount);
    }

    public Film getFilmById(Long id) {
        return containsFilm(id);
    }

    public Film addFilm(Film newFilm) {
        return filmStorage.createFilm(newFilm);
    }

    public Film updateFilm(Film filmUp) {
        return filmStorage.update(filmUp);
    }

    public void addLike(Long filmId, Long userId) {
        log.info("Запрос пользователя с id: {} поставить лайк фильму с id {}", userId, filmId);
        Film film = containsFilm(filmId);
        User user = userStorage.getUserById(userId);
        film.addLike(userId);
        filmStorage.update(film);
        log.debug("Пользователь {} успешно поставил лайк фильму с id {}", userId, filmId);
    }

    public void removeLike(Long filmId, Long userId) {
        log.info("Запрос пользователя с id: {} удалить лайк фильму с id {}", userId, filmId);
        Film film = containsFilm(filmId);
        User user = userStorage.getUserById(userId);
        film.removeLike(userId);
        filmStorage.update(film);
    }

    private Film containsFilm(Long filmId) {
        Film film = filmStorage.getFilmById(filmId);
        if (film == null) {
            log.debug("Фильм с id {} не был найден", filmId);
            throw new NotFoundException("Фильм с id " + filmId + " не был найден");
        }
        return film;
    }

    private Collection<Film> sortFilms() {
        Collection<Film> films = List.copyOf(filmStorage.getFilms());
        log.info("Сортировка фильмов по количеству лайков {}", films);
        return films.stream()
                .sorted(Comparator.comparing((Film film) -> film.getLikes() == null ? 0 : film.getLikes().size())
                        .reversed())
                .collect(Collectors.toList());
    }
}
