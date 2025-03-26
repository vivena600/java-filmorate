package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder(toBuilder = true)
@Data
public class Film {
    private static final int MAX_SIZE_DESCRIPTION = 200;
    private Long id;

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
