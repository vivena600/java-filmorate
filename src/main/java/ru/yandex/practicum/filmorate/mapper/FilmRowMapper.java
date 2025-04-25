package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.mpa.DbRetingStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

public class FilmRowMapper implements RowMapper<Film> {
    private DbRetingStorage dbMpa;

    public FilmRowMapper(DbRetingStorage dbMpa) {
        this.dbMpa = dbMpa;
    }

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film =  Film.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(rs.getObject("mpa_id") != null ?
                        dbMpa.getRatingById(rs.getInt("mpa_id")) : null)
                .build();
        return film;
    }
}
