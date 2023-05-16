package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage{
    private Map<Integer, User> users = new HashMap<>();
    private int id = 0;

    @Override
    public List<User> getUsersList() {
        return users.values().stream().collect(Collectors.toList());
    }

    @Override
    public User create(User user) {
        id++;
        user.setId(id);
        users.put(user.getId(), user);

        return user;
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);

        return user;
    }

    @Override
    public void delete(User user) {
        users.remove(user.getId());
    }

    @Override
    public User getUserById(int id) {
        User user = users.get(id);
        if (user == null) {
            throw new UserNotFoundException(String.format("Пользователь с id = %d не найден", id));
        }
        return user;
    }

    @Override
    public User searchUserByEmail(String email) {
        return users.values().stream()
                .filter(p -> email.equals(p.getEmail()))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(String.format("Пользователь %s не найден", email)));
    }
}
