package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenresService;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenresController {
    private final GenresService genresService;

    @GetMapping
    public Collection<Genre> getAllGenres() {
        return genresService.getGenres();
    }

    @GetMapping("{id}")
    public Genre getGenreById(@PathVariable int id) {
        return genresService.getGenreById(id);
    }
}
