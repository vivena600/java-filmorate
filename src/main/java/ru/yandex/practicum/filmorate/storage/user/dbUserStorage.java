package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class dbUserStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    

    @Override
    public User createUser(User newUser) {
        String query = "INSERT INTO users (name, email, login, birthday) VALUES (?, ?, ?, ?)";
        KeyHolder generatedKeys = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(query, new String[]{"id"});

            ps.setString(1, newUser.getName());
            ps.setString(2, newUser.getEmail());
            ps.setString(3, newUser.getLogin());
            ps.setObject(4, newUser.getBirthday());

            return ps;
        }, generatedKeys);

        return null;
    }

    @Override
    public void deleteUser(User user) {

    }

    @Override
    public User updateUser(User userUp) {
        return null;
    }

    @Override
    public User getUserById(Long userId) {
        return null;
    }

    @Override
    public Collection<User> getUsers() {
        return List.of();
    }
}
