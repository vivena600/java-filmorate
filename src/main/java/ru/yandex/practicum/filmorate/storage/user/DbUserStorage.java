package ru.yandex.practicum.filmorate.storage.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class DbUserStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

    @Override
    public User createUser(UserDto newUser) {
        String query = "INSERT INTO users (name, login, email, birthday) VALUES (?, ?, ?, ?)";
        KeyHolder generatedKeys = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(query, new String[]{"id"});

                ps.setString(1, newUser.getName());
                ps.setString(2, newUser.getLogin());
                ps.setString(3, newUser.getEmail());
                ps.setDate(4, Date.valueOf(newUser.getBirthday()));

                return ps;
            }, generatedKeys);

            long userId = generatedKeys.getKey().longValue();
            newUser.setId(userId);

            return userMapper.mapToUser(newUser);
        } catch (DataAccessException ex) {
            log.error("ошибка при создании пользователя {}", ex.getMessage());
            throw new RuntimeException("Не удалось создать пользователя");
        }
    }


    @Override
    public void deleteUser(UserDto user) {

    }

    @Override
    public User updateUser(UserDto userUp) {
        return null;
    }

    @Override
    public User getUserById(Long userId) {
        return null;
    }

    @Override
    public Collection<User> getUsers() {
        log.info("Запрос на полученнние информации о всех пользователей");
        String query = "SELECT * FROM users";
        return jdbcTemplate.query(query, new UserRowMapper());
    }
}
