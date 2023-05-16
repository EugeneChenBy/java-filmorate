package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    public List<User> getUsersList();

    public User create(User user);

    public User update(User user);

    public void delete(User user);

    public User getUserById(int id);

    public User searchUserByEmail(String email);
}
