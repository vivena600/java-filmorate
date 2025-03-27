package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class User {
    private Long id;
    private Set<Long> friends;

    @NotBlank
    @Email
    private  String email;

    @NotBlank
    private String login;

    private String name;

    @NotNull
    @Past
    private LocalDate birthday;

    public void addFriend(Long friendId) {
        if (friends == null) {
            friends = new HashSet<>();
        }
        friends.add(friendId);
    }

    public void deleteFriend(Long friendId) {
        if (friends == null) {
            friends = new HashSet<>();
        }
        friends.remove(friendId);
    }
}
