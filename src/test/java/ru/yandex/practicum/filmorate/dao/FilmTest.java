package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.RatingRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenresDao;
import ru.yandex.practicum.filmorate.storage.MpaDao;
import ru.yandex.practicum.filmorate.storage.film.FilmDao;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({ FilmDao.class,
        FilmMapper.class,
        RatingRowMapper.class,
        MpaDao.class,
        GenresDao.class})
public class FilmTest {
    private final FilmDao filmDao;
    private final GenresDao genresDao;
    private static FilmDto film;
    private Long i = 0L;

    @BeforeEach
    public void startTest() {
        film = FilmDto.builder()
                .name("film name" + i)
                .duration(3)
                .description("description film's")
                .releaseDate(LocalDate.parse("2025-03-25"))
                .build();
        i++;
    }

    @DisplayName("Заполнение основных полей фильма")
    @Test
    void testFilingTheMainDataFilm() {
        Film createFilm = filmDao.createFilm(film);
        Collection<Film> allFilms = filmDao.getFilms();
        assertEquals(allFilms.size(), i, "Фильм не был создан");
        assertThat(createFilm).hasFieldOrPropertyWithValue("id", film.getId());
        assertThat(createFilm).hasFieldOrPropertyWithValue("name", film.getName());
        assertThat(createFilm).hasFieldOrPropertyWithValue("description", film.getDescription());
        assertThat(createFilm).hasFieldOrPropertyWithValue("releaseDate", film.getReleaseDate());
        assertThat(createFilm).hasFieldOrPropertyWithValue("genres", null);
        assertThat(createFilm).hasFieldOrPropertyWithValue("mpa", null);

    }

    @DisplayName("Создание фильма с жанрами")
    @Test
    void testCreateFilmWithGenresList() {
        FilmDto filmDto = film;
        Genre genre1 = genresDao.getGenreById(1);
        Genre genre2 = genresDao.getGenreById(2);
        Genre genre3 = genresDao.getGenreById(1); //дубликат genre1
        Collection<Genre> genres = Arrays.asList(genre1, genre2, genre3);
        filmDto.setGenres(new HashSet<>(genres));
        Collection<Genre> resultGenres = Arrays.asList(genre1, genre2);
        Film createFilm = filmDao.createFilm(filmDto);
        Collection<Film> allFilms = filmDao.getFilms();
        assertEquals(allFilms.size(), i, "Фильм не был создан");
        assertThat(createFilm).hasFieldOrPropertyWithValue("id", filmDto.getId());
        assertThat(createFilm).hasFieldOrPropertyWithValue("name", filmDto.getName());
        assertThat(createFilm).hasFieldOrPropertyWithValue("description", filmDto.getDescription());
        assertThat(createFilm).hasFieldOrPropertyWithValue("releaseDate", filmDto.getReleaseDate());
        assertEquals(resultGenres.size(), filmDto.getGenres().size(), "количество жанров не совпадает");
        assertThat(createFilm).hasFieldOrPropertyWithValue("mpa", null);
    }

    @DisplayName("Удаление фильма")
    @Test
    void testDeleteFilm() {
        filmDao.createFilm(film);
        Collection<Film> allFilms = filmDao.getFilms();
        filmDao.deleteFilm(film);
        Collection<Film> allFilmsBeforeDelete = filmDao.getFilms();
        assertEquals(allFilmsBeforeDelete.size(), allFilms.size()-1, "Фильм не был удален");
        i--;
    }

    @DisplayName("Получение фильма по id")
    @Test
    void testGetFilmById() {
        Film createdFilm = filmDao.createFilm(film);
        Film foundFilm = filmDao.getFilmById(createdFilm.getId());
        assertThat(foundFilm).hasFieldOrPropertyWithValue("id", createdFilm.getId());
        assertThat(foundFilm).hasFieldOrPropertyWithValue("name", createdFilm.getName());
        assertThat(foundFilm).hasFieldOrPropertyWithValue("description", createdFilm.getDescription());
        assertThat(foundFilm).hasFieldOrPropertyWithValue("releaseDate",createdFilm.getReleaseDate());
        assertThat(foundFilm).hasFieldOrPropertyWithValue("mpa", null);
    }
}
