package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class User {
    private Long id;
    //private Set<Long> friends;

    @NotBlank
    @Email
    private  String email;

    @NotBlank
    private String login;

    private String name;

    @NotNull
    @Past
    private LocalDate birthday;
}
