package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenresDao;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class GenresService {
    private static final Logger log = LoggerFactory.getLogger(GenresService.class);
    private final GenresDao dbGenres;

    public Collection<Genre> getGenres() {
        return dbGenres.getGenres();
    }

    public Genre getGenreById(int id) {
        return dbGenres.getGenreById(id);
    }
}
