package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class UserDto {
    private Long id;
    @NotBlank
    @Email
    private String email;
    private String name;
    @NotBlank
    private String login;
    @NotNull
    @Past
    private LocalDate birthday;
}
