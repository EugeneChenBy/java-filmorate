package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User getUserElseThrow(int id) {
        try {
            return userStorage.getUserById(id);
        } catch (EmptyResultDataAccessException e) {
            String text = "Не найден пользователь с id = " + id;
            throw new UserNotFoundException(text);
        }
    }

    public User create(User user) {
        validate(user);

        return userStorage.create(user);
    }

    public User update(User user) {
        validate(user);

        User userTmp = getUserElseThrow(user.getId());

        return userStorage.update(user);
    }

    public List<User> getAllUsers() {
        return userStorage.getUsersList();
    }

    public User getUserById(int id) {
        return getUserElseThrow(id);
    }

    public void addFriend(int userId, int friendId) {
        User user = getUserElseThrow(userId);
        User friend = getUserElseThrow(friendId);

        addFriend(user, friend);
    }

    private void addFriend(User user1, User user2) {
        userStorage.addFriend(user1.getId(), user2.getId());
    }

    public void removeFriend(int userId, int friendId) {
        User user = getUserElseThrow(userId);
        User friend = getUserElseThrow(friendId);

        removeFriend(user, friend);
    }

    private void removeFriend(User user1, User user2) {
        userStorage.removeFriend(user1.getId(), user2.getId());
    }

    public List<User> getFriends(int userId) {
        User user = getUserElseThrow(userId);

        return getFriends(user);
    }

    public List<User> getFriends(User user) {
        return userStorage.getFriends(user.getId());
    }

    public List<User> getCommonFriends(int userId, int otherUserId) {
        User user = getUserElseThrow(userId);
        User otherUser = getUserElseThrow(otherUserId);

        return getCommonFriends(user, otherUser);
    }

    private List<User> getCommonFriends(User user1, User user2) {
        return userStorage.getCommonFriends(user1.getId(), user2.getId());
    }

    private void validate(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
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
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
