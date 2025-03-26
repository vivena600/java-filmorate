package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.ConditionNotMetException;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private static final LocalDate MINREASEDATA = LocalDate.of(1895, 12, 28);
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getFilms() {
        return List.copyOf(films.values());
    }

    @PostMapping
    public Film postFilm(@Valid @RequestBody final Film newFilm) {
        log.debug("Запрос на добавление фильма {}", newFilm.toString());
        validatorReleaseDate(newFilm);

        newFilm.setId(getNextId());
        films.put(newFilm.getId(), newFilm);
        log.debug("Фильм добавлен {}", newFilm.toString());
        return newFilm;
    }

    @PutMapping
    public Film putFilm(@Valid @RequestBody final Film newFilm) {
        log.debug("Запрос на обнавление данных о фильме {}", newFilm.toString());
        if (newFilm.getId() == null) {
            log.trace("В запросе не указан id фильма {}", newFilm.toString());
            throw new ConditionNotMetException("Id не должен быть пустым");
        }

        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            log.trace("Найдем фильм данные которого будем обнавлять {}", oldFilm.toString());
            validatorReleaseDate(newFilm);
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setName(newFilm.getName());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            oldFilm.setDuration(newFilm.getDuration());
            return oldFilm;
        }
        log.debug("Фильм с id {} не была найден", newFilm.getId());
        throw new NotFoundException("Фильм с id " + newFilm.getId() + " не был найден");
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

    private void validatorReleaseDate(final Film newFilm) {
        if (newFilm.getReleaseDate().isBefore(MINREASEDATA)) {
            log.trace("Дата релиза {} раньше миниальной даты {}", newFilm, MINREASEDATA);
            throw new ConditionNotMetException("Дата релиза должна быть не реньше " + MINREASEDATA);
        }
    }
}
