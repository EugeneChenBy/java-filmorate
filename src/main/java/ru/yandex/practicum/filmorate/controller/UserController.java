package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class UserController {
    Map<Integer, User> users = new HashMap<>();
    private int id = 0;

    @PostMapping("/users")
    public User create(@RequestBody User user) {
        log.info("Получен POST-запрос на создание пользователя - {}", user);

        try {
            validate(user);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ValidationException(e.getMessage());
        }

        id++;
        user.setId(id);
        users.put(user.getId(), user);

        return user;
    }

    @PutMapping("/users")
    public User update(@RequestBody User user) {
        log.info("Получен PUT-запрос на обновление пользователя - {}", user);

        try {
            validate(user);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ValidationException(e.getMessage());
        }

        if (!users.containsKey(user.getId())) {
            String text = "Не найден пользователь с id = " + user.getId();
            log.error(text);
            throw new ValidationException(text);
        }
        users.put(user.getId(), user);

        return user;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return users.values().stream().collect(Collectors.toList());
    }

    private void validate(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new ValidationException("Email не может быть пустым");
        }
        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Email должен содержать @");
        }
        if (user.getLogin() == null || user.getLogin().isEmpty()) {
            throw new ValidationException("Логин не может быть пустым");
        }
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может содержать пробелы");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
