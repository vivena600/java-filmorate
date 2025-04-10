package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public Collection<Film> getFilms() {
        return filmService.getFilms();
    }

    @GetMapping("{id}")
    public Film getFilmById(@PathVariable final Long id) {
        return filmService.getFilmById(id);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilm(@RequestParam(defaultValue = "10") @Positive int count) {
        return filmService.getPopularFilms(count);
    }

    @PostMapping
    public Film postFilm(@Valid @RequestBody final Film newFilm) {
        return filmService.addFilm(newFilm);
    }

    @PutMapping
    public Film putFilm(@Valid @RequestBody final Film newFilm) {
        return filmService.updateFilm(newFilm);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLikes(@PathVariable final Long id, @PathVariable final Long userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLikes(@PathVariable final Long id, @PathVariable final Long userId) {
        filmService.removeLike(id, userId);
    }
}
