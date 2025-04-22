package ru.yandex.practicum.filmorate.dto;

import lombok.Builder;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.yandex.practicum.filmorate.model.Genre;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class FilmDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private Set<Long> likes;
    private Set<Genre> generes;
    private Integer ratingId;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
}
