package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.DbGeneres;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class GenresService {
    private static final Logger log = LoggerFactory.getLogger(GenresService.class);
    private final DbGeneres dbGeneres;

    public Collection<Genre> getGenres() {
        return dbGeneres.getGenres();
    }

    public Genre getGenreById(int id) {
        return dbGeneres.getGenreById(id);
    }
}
