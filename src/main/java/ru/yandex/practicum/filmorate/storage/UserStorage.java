package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    public List<User> getUsersList();

    public User create(User user);

    public User update(User user);

    public void delete(User user);

    public User getUserById(int id);

    public  List<User> getFriends(int id);

    public List<User> getCommonFriends(int userId, int otherUserId);

    public void addFriend(int user1_id, int user2_id);

    public void removeFriend(int user1_id, int user2_id);
}
