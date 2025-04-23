package ru.yandex.practicum.filmorate.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenreDto {
    private int genere_id;
    private String name;
}
