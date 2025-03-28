package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ConditionNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private static final LocalDate MINREASEDATA = LocalDate.of(1895, 12, 28);
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Film createFilm(Film newFilm) {
        log.info("Запрос на добавление фильма {}", newFilm.toString());
        validatorReleaseDate(newFilm);

        newFilm.setId(getNextId());
        films.put(newFilm.getId(), newFilm);
        log.debug("Фильм успешно добавлен {}", newFilm.toString());
        return newFilm;
    }

    @Override
    public void deleteFilm(Film film) {
        log.info("Запрос на удаление фильма - {}", film);
        if (getFilmById(film.getId()) != null) {
            films.remove(film.getId());
            log.debug("Фильм {} был удален", film);
        }
    }

    @Override
    public Film update(Film filmUp) {
        log.info("Запрос на обнавление фильма: {}", filmUp.toString());
        if (filmUp.getId() == null) {
            log.warn("В запросе не указан id фильма {}", filmUp.toString());
            throw new ConditionNotMetException("Id не должен быть пустым");
        }

        Film oldFilm = getFilmById(filmUp.getId());
        log.trace("Найден фильм, информацию о котором необходимо обновить: {}", oldFilm.toString());
        validatorReleaseDate(filmUp);
        oldFilm.setDescription(filmUp.getDescription());
        oldFilm.setName(filmUp.getName());
        oldFilm.setReleaseDate(filmUp.getReleaseDate());
        oldFilm.setDuration(filmUp.getDuration());
        films.put(oldFilm.getId(), oldFilm);
        log.debug("Обновленная информация о фильме: {}", oldFilm);
        return oldFilm;
    }

    @Override
    public Collection<Film> getFilms() {
        log.info("Запрос на получение всех фильмов");
        return List.copyOf(films.values());
    }

    @Override
    public Film getFilmById(Long filmId) {
        log.info("Запрос на получение фильма по id {}", filmId);
        if (!films.containsKey(filmId)) {
            log.warn("Не удалось найти фильм по его id: {}", filmId);
            throw new NotFoundException("Фильм с id: " + filmId + " не был найден");
        }
        log.debug("Информация о фильме с id {} была успешно получена: {}", filmId, films.get(filmId));
        return films.get(filmId);
    }

    private void validatorReleaseDate(final Film newFilm) {
        log.trace("Проверяем фильм на соответсвие даты релиза");
        if (newFilm.getReleaseDate().isBefore(MINREASEDATA)) {
            log.warn("Дата релиза {} раньше миниальной даты {}", newFilm, MINREASEDATA);
            throw new ConditionNotMetException("Дата релиза должна быть не реньше " + MINREASEDATA);
        }
    }

    private Long getNextId() {
        int currentMaxId = (int) films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        currentMaxId++;
        return (long) currentMaxId;
    }
}
