package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder(toBuilder = true)
@Data
public class User {
    private Long id;

    @NotNull
    @NotBlank
    @Email
    private  String email;

    @NotNull
    @NotBlank
    private String login;

    private String name;

    @NotNull
    @Past
    private LocalDate birthday;
}
