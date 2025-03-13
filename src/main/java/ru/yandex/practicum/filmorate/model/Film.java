package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder(toBuilder = true)
@Data
public class Film {
    private final static int MAXSIZEDESCRIPTION = 200;

    private Long id;

    @NotNull(message = "У фильма должно быть название")
    @NotBlank(message = "название не должно быть пустым")
    private String name;

    @NotNull(message = "У фильма должно быть описание")
    @NotBlank(message = "Описание фильма не может быть пустым")
    @Size(min = 1, max = MAXSIZEDESCRIPTION, message = "Описание фильма не должно превышать " + MAXSIZEDESCRIPTION)
    private String description;

    @NotNull
    private LocalDate releaseDate;

    @NotNull
    @Positive(message = "Продолжительность фильма должна быть положительным числом")
    private int duration;
}
