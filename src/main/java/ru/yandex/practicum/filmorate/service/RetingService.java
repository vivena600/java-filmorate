package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Reting;
import ru.yandex.practicum.filmorate.storage.mpa.DbRetingStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class RetingService {
    private static final Logger log = LoggerFactory.getLogger(RetingService.class);
    private final DbRetingStorage retingSt;

    public Collection<Reting> getRatings() {
        return retingSt.getRatings();
    }

    public Reting getRatingById(int id) {
        return retingSt.getRatingById(id);
    }

}
