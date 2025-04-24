package ru.yandex.practicum.filmorate.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.dto.RatingDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Reting;
import ru.yandex.practicum.filmorate.storage.DbGeneres;
import ru.yandex.practicum.filmorate.storage.mpa.DbMpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

/*
@RequiredArgsConstructor
public class FilmRowMapper implements RowMapper<Film> {
    private final DbGeneres dbGeneres;
    private final DbMpa dbMpa;


    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        //Reting reting = dbMpa.getRatingById(rs.getInt("genre_id"));
        return Film.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("releaseDate").toLocalDate())
                .duration(rs.getInt("duration"))
                //.generes((Set<Genre>) reting)
                .mpa(rs.getObject("reting_id", RatingDto.class))
                .build();
    }
}

 */
