package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Reting;
import ru.yandex.practicum.filmorate.storage.MpaDao;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({MpaDao.class})
public class MpaTest {
    private final MpaDao dao;

    private static final int SIZE_MPA_LIST = 5;

    @DisplayName("Получение всех mpa")
    @Test
    void testGetAllMpa() {
        Collection<Reting> mpa = dao.getRatings();
        assertNotNull(mpa, "Возвращает пустой объект");
        assertEquals(mpa.size(), SIZE_MPA_LIST, "Количество возрастных ограничений не совпадает");
    }

    @DisplayName("Получение mpa по id")
    @Test
    void testGetMpaById() {
        Reting reting = dao.getRatingById(1);
        assertNotNull(reting, "Возвращает пустой объект");
        assertThat(reting).hasFieldOrPropertyWithValue("id", 1);
        assertThat(reting).hasFieldOrPropertyWithValue("name", "G");
    }
}
