package ru.yandex.practicum.filmorate.dto;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.enums.RatingType;

@Data
@Builder
public class RatingDto {
    private int id;
    private String name;
}
