package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public User postUser(@Valid @RequestBody final UserDto newUser) {
        return userService.createUser(newUser);
    }

    @GetMapping
    public Collection<User> getUsers() {
        return userService.getUsers();
    }


    @GetMapping("{id}")
    public User getUserByID(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping
    public User putUser(@Valid @RequestBody final UserDto userUp) {
        return userService.updateUser(userUp);
    }

    /*

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable final Long id) {
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFrends(@PathVariable final Long id, @PathVariable final Long otherId) {
        return userService.retainFriends(id, otherId);
    }


    @PutMapping("{id}/friends/{friendId}")
    public void addFriends(@PathVariable final Long id,
            @PathVariable final Long friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void deleteFriends(@PathVariable final Long id,
                              @PathVariable final Long friendId) {
        userService.deleteFriend(id, friendId);
    }

     */
}
