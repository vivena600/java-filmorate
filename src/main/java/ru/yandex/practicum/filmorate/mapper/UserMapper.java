package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public class UserMapper {

    public static UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .login(user.getLogin())
                .email(user.getEmail())
                .birthday(user.getBirthday())
                .build();
    }

    public static User mapToUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .login(userDto.getLogin())
                .email(userDto.getEmail())
                .birthday(userDto.getBirthday())
                .build();
    }
}
