package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public User create(@RequestBody User user) {
        log.info("Получен POST-запрос на создание пользователя - {}", user);

        return userService.create(user);
    }

    @PutMapping("/users")
    public User update(@RequestBody User user) {
        log.info("Получен PUT-запрос на обновление пользователя - {}", user);

        return userService.update(user);
    }

    @GetMapping("/users/{id}")
    public User findById(@PathVariable("id") int userId) {
        return userService.getUserById(userId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getUserFriends(@PathVariable("id") int userId) {
        return userService.getFriends(userId);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") int userId, @PathVariable("otherId") int otherUserId) {
        return userService.getCommonFriends(userId, otherUserId);
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") int userId, @PathVariable("friendId") int friendId) {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") int userId, @PathVariable("friendId") int friendId) {
        userService.removeFriend(userId, friendId);
    }
}
