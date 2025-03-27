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
        log.debug("Запрос на добавление фильма {}", newFilm.toString());
        validatorReleaseDate(newFilm);

        newFilm.setId(getNextId());
        films.put(newFilm.getId(), newFilm);
        log.debug("Фильм добавлен {}", newFilm.toString());
        return newFilm;
    }

    @Override
    public void deleteFilm(Film film) {
        log.debug("Запрос на удаление фильма - {}", film);
        if (getFilmById(film.getId()) != null) {
            films.remove(film.getId());
            log.debug("Фильм {} был удален", film);
        }
    }

    @Override
    public Film update(Film filmUp) {
        log.debug("Запрос на обнавление фильма: {}", filmUp.toString());
        if (filmUp.getId() == null) {
            log.trace("В запросе не указан id фильма {}", filmUp.toString());
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
        return oldFilm;
    }

    @Override
    public Collection<Film> getFilms() {
        return List.copyOf(films.values());
    }

    @Override
    public Film getFilmById(Long filmId) {
        if (!films.containsKey(filmId)) {
            log.trace("Не удалось найти фильм по его id: {}", filmId);
            throw new NotFoundException("Фильм с id: " + filmId + " не был найден");
        }
        return films.get(filmId);
    }

    private void validatorReleaseDate(final Film newFilm) {
        if (newFilm.getReleaseDate().isBefore(MINREASEDATA)) {
            log.trace("Дата релиза {} раньше миниальной даты {}", newFilm, MINREASEDATA);
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
