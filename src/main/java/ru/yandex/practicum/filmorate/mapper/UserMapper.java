package ru.yandex.practicum.filmorate.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;

@Component
public class UserMapper {
    public static UserDto mapToUserDto(UserDto user) {
        return UserDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .birthday(user.getBirthday())
                .friends(user.getFriends())
                .build();
    }

    private static User mapToUser(UserDto userDto) {
        return User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .birthday(userDto.getBirthday())
                .friends(userDto.getFriends())
                .build();
    }
}
