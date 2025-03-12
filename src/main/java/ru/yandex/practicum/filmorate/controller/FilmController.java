package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.ConditionNotMetException;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private static final int MAXLENDESCRIPTION = 200;
    private static final LocalDate MINREASEDATA = LocalDate.of(1895, 12, 28);
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }

    @PostMapping
    public Film postFilm(@Valid @RequestBody Film newfilm) {
        if (newfilm.getName() == null || newfilm.getName().isBlank()) {
            throw new ConditionNotMetException("Название фильма не может отсутсвовать");
        }
        if (newfilm.getDescription().length() > MAXLENDESCRIPTION) {
            throw new ConditionNotMetException("Превышана максимальная длина описания фильма");
        }
        /*
        if (newfilm.getDuration() < 0) {
            throw  new ConditionNotMetException("Продолжительность фильма должна быть положительным числом");
        }

         */

        if (newfilm.getReleaseDate() == null || newfilm.getReleaseDate().isBefore(MINREASEDATA)) {
            throw new ConditionNotMetException("Дата релиза должна быть не реньше " + MINREASEDATA);
        }

        newfilm.setId(getNextId());
        films.put(newfilm.getId(), newfilm);
        return newfilm;
    }

    @PutMapping
    public Film putFilm(@Valid @RequestBody Film newFilm) {
        if (newFilm.getId() == null) {
            throw new ConditionNotMetException("Id не должен быть пустым");
        }

        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            if (newFilm.getName() == null || newFilm.getName().isBlank()) {
                throw new ConditionNotMetException("Название фильма не может отсутсвовать");
            }
            if (newFilm.getDescription().length() > MAXLENDESCRIPTION) {
                throw new ConditionNotMetException("Превышана максимальная длина описания фильма");
            }
            /*
            if (newFilm.getDuration() < 0) {
                throw  new ConditionNotMetException("Продолжительность фильма должна быть положительным числом");
            }

             */
            if (newFilm.getReleaseDate() == null || newFilm.getReleaseDate().isBefore(MINREASEDATA)) {
                throw new ConditionNotMetException("Дата релиза должна быть не реньше " + MINREASEDATA);
            }
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setName(newFilm.getName());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            oldFilm.setDuration(newFilm.getDuration());
            return oldFilm;
        }
        throw new NotFoundException("Фильм с id " + newFilm.getId() +" не был найден");
    }

    private Long getNextId() {
        int currentMaxId = (int) films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        currentMaxId ++;
        return (long) currentMaxId;
    }
}
