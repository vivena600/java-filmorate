package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.Reting;

import java.util.Collection;

public interface DbMpa {
    public Collection<Reting> getRatings();

    public Reting getRatingById(int id);
}
