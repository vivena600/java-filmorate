package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.LikeDao;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeDao likeDao;

    public Film addFilm(FilmDto newFilm) {
        return filmStorage.createFilm(newFilm);
    }

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilmById(Long id) {
        return filmStorage.getFilmById(id);
    }

    public Film updateFilm(FilmDto filmUp) {
        return filmStorage.update(filmUp);
    }

    public void addLike(Long filmId, Long userId) {
        likeDao.addLike(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        likeDao.removeLike(filmId, userId);
    }

    /*


    public Collection<Film> getPopularFilms(int count) {
        log.info("Запрос на получение {} популярных фильмов", count);
        int newCount = count;
        Collection<Film> sortFilmList = sortFilms();
        if (count > sortFilmList.size()) {
            newCount = sortFilmList.size();
        }
        log.trace("Введеное count больше количества фильмов, count изменен на {}", newCount);
        return sortFilmList.stream().toList().subList(0, newCount);
    }


    private Film containsFilm(Long filmId) {
        log.trace("Проверка информации о фильме с id {}", filmId);
        Film film = filmStorage.getFilmById(filmId);
        if (film == null) {
            log.debug("Фильм с id {} не был найден", filmId);
            throw new NotFoundException("Фильм с id " + filmId + " не был найден");
        }
        return film;
    }

    private Collection<Film> sortFilms() {
        Collection<Film> films = List.copyOf(filmStorage.getFilms());
        log.trace("Сортировка фильмов по количеству лайков {}", films);
        return films.stream()
                .sorted(Comparator.comparing((Film film) -> film.getLikes() == null ? 0 : film.getLikes().size())
                        .reversed())
                .collect(Collectors.toList());
    }

     */
}
