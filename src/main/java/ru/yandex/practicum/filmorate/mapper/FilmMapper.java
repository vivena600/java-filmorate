package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.FilmDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.Film;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FilmMapper{

    public static Film mapToFilm(FilmDto filmDto) {
        return Film.builder()
                .name(filmDto.getName())
                .description(filmDto.getDescription())
                .releaseDate(filmDto.getReleaseDate())
                .duration(filmDto.getDuration())
                .likes(filmDto.getLikes())
                .ratingId(filmDto.getRatingId())
                .generes(filmDto.getGeneres())
                .build();
    }

    public static FilmDto mapToFilmDto(Film film) {
        return FilmDto.builder()
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .likes(film.getLikes())
                .ratingId(film.getRatingId())
                .generes(film.getGeneres())
                .build();
    }
}
