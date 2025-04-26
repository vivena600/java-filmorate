package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.LikeDao;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

@Service
@Slf4j
@RequiredArgsConstructor
public class LikeService {
    private final LikeDao likeDao;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public void addLike(Long filmId, Long userId) {
        log.info("Запрос пользователя с id: {} поставить лайк фильму с id {}", userId, filmId);
        filmStorage.chekFilmId(filmId);
        userStorage.checkUserId(userId);

        likeDao.addLike(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        log.info("Запрос пользователя с id: {} удалить лайк фильму с id {}", userId, filmId);
        filmStorage.chekFilmId(filmId);
        userStorage.checkUserId(userId);

        likeDao.removeLike(filmId, userId);
    }
}
