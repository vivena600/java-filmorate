package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.filmorate.enums.RatingType;

@Getter
@Setter
@AllArgsConstructor
public class Rating {
    private int id;
    private RatingType name;
}
