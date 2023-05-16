package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) {
        try {
            validate(user);
        } catch (ValidationException e) {
            log.error(e.getMessage());
            throw new ValidationException(e.getMessage());
        }

        user.setFriends(new HashSet<>());

        return userStorage.create(user);
    }

    public User update(User user) {
        try {
            validate(user);
        } catch (ValidationException e) {
            log.error(e.getMessage());
            throw new ValidationException(e.getMessage());
        }

        throwIfUserNull(userStorage.getUserById(user.getId()), user.getId());

        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }

        return userStorage.update(user);
    }

    public List<User> getAllUsers() {
        return userStorage.getUsersList();
    }

    public User getUserById(int id) {
        User user = userStorage.getUserById(id);

        throwIfUserNull(user, id);

        return user;
    }

    public void addFriend(int userId, int friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);

        throwIfUserNull(user, userId);
        throwIfUserNull(friend, friendId);

        addFriend(user, friend);
    }

    private void addFriend(User user1, User user2) {
        Set<Integer> friends1 = user1.getFriends();
        friends1.add(user2.getId());
        user1.setFriends(friends1);

        Set<Integer> friends2 = user2.getFriends();
        friends2.add(user1.getId());
        user2.setFriends(friends2);

        userStorage.update(user1);
        userStorage.update(user2);
    }

    public void removeFriend(int userId, int friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);

        throwIfUserNull(user, userId);
        throwIfUserNull(friend, friendId);

        removeFriend(user, friend);
    }

    private void removeFriend(User user1, User user2) {
        Set<Integer> friends1 = user1.getFriends();
        friends1.remove(user2.getId());
        user1.setFriends(friends1);

        Set<Integer> friends2 = user2.getFriends();
        friends2.remove(user1.getId());
        user2.setFriends(friends2);

        userStorage.update(user1);
        userStorage.update(user2);
    }

    public List<User> getFriends(int userId) {
        User user = userStorage.getUserById(userId);

        throwIfUserNull(user, userId);

        return getFriends(user);
    }

    public List<User> getFriends(User user) {
        return getUserListFromIdList(user.getFriends().stream().collect(Collectors.toList()));
    }

    public List<User> getCommonFriends(int userId, int otherUserId) {
        User user = userStorage.getUserById(userId);
        User otherUser = userStorage.getUserById(otherUserId);

        throwIfUserNull(user, userId);
        throwIfUserNull(otherUser, otherUserId);

        return getCommonFriends(user, otherUser);
    }

    private List<User> getCommonFriends(User user1, User user2) {
        List<Integer> listFriends1 = user1.getFriends().stream().collect(Collectors.toList());
        List<Integer> listFriends2 = user2.getFriends().stream().collect(Collectors.toList());

        List<Integer> listCommonIdFriends = listFriends1.stream().filter(listFriends2::contains).collect(Collectors.toList());

        return getUserListFromIdList(listCommonIdFriends);
    }

    private List<User> getUserListFromIdList(List<Integer> listInt) {
        return listInt.stream().map(p -> userStorage.getUserById(p)).collect(Collectors.toList());
    }

    private void throwIfUserNull(User user, int userId) {
        if (user == null) {
            String text = "Не найден пользователь с id = " + userId;
            log.error(text);
            throw new UserNotFoundException(text);
        }
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
