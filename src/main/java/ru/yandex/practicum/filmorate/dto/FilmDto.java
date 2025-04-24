package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Reting;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class FilmDto {
    private static final int MAX_SIZE_DESCRIPTION = 200;
    private Long id;
    private Set<Genre> genres;
    private Reting mpa;
    @NotBlank(message = "название не должно быть пустым")
    private String name;
    @NotBlank(message = "Описание фильма не может быть пустым")
    @Size(min = 1, max = MAX_SIZE_DESCRIPTION, message = "Описание фильма не должно превышать " + MAX_SIZE_DESCRIPTION)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @NotNull
    @Positive(message = "Продолжительность фильма должна быть положительным числом")
    private int duration;
}
