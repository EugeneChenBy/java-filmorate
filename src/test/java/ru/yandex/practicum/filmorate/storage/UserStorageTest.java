package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserStorageTest {
    @Autowired
    UserDBStorage storage;
    @Autowired
    JdbcTemplate jdbcTemplate;

    List<User> users;
    User user1;
    User user2;
    User user3;
    User user4;
    User user5;

    private void newDBData() {
        jdbcTemplate.update("DELETE FROM users");
        jdbcTemplate.update("DELETE FROM friend");

        users = new ArrayList<>();

        user1 = User.builder()
                 .email("mail1@mail.ru")
                 .login("eugene_chen")
                 .name("Eugene Chen")
                 .birthday(LocalDate.of(1988, 11, 14))
                 .build();
        user2 = User.builder()
                 .email("mail2@mail.ru")
                 .login("a.tolkachev")
                 .name("Anton Tolkachev")
                 .birthday(LocalDate.of(1990, 05, 20))
                 .build();
        user3 = User.builder()
                 .email("mail3@mail.ru")
                 .login("p.shirinkina")
                 .name("Polina Shirinkina")
                 .birthday(LocalDate.of(1998, 05, 01))
                 .build();
        user4 = User.builder()
                 .email("mail4@mail.ru")
                 .login("c.friend")
                 .name("Common Friend")
                 .birthday(LocalDate.of(1991, 04, 11))
                 .build();
        user5 = User.builder()
                 .email("mail4@mail.ru")
                 .login("somebody")
                 .name("Some Body")
                 .birthday(LocalDate.of(2000, 01, 01))
                 .build();
    }

    @BeforeEach
    void beforeEach() {
        newDBData();
    }

    @Test
    void shouldGetUsersList() {
        users = List.of(user1, user2, user3, user4, user5);

        storage.create(user1);
        storage.create(user2);
        storage.create(user3);
        storage.create(user4);
        storage.create(user5);

        List<User> real = storage.getUsersList();

        assertEquals(users, real, "Полученный список пользователей не равен добавленному и хранимому в БД");
    }

    @Test
    void shouldCreateUser() {
        User userAdded = storage.create(user1);

        assertEquals(user1, userAdded, "Добавленный пользователь не равен добавляемому");
    }

    @Test
    void shouldUpdateUser() {
        storage.create(user1);

        user1.setName("Евгений Мисюченко");
        storage.update(user1);

        User userUpdated = storage.getUserById(user1.getId());

        assertEquals(user1, userUpdated, "Пользователь не обновился");
    }

    @Test
    void shouldDeleteUser() {
        storage.create(user1);

        storage.delete(user1);

        assertThrows(EmptyResultDataAccessException.class, () -> storage.getUserById(user1.getId()));
    }

    @Test
    void shouldGetUserById() {
        User user1Added = storage.create(user1);

        User user1Check = storage.getUserById(1);
        assertEquals(user1, user1Check, "Добавленный пользователь не равен найденному по id");
    }

    @Test
    void shouldThrowGetUserByWrongId() {
        assertThrows(EmptyResultDataAccessException.class, () -> storage.getUserById(50));
    }

    @Test
    void shouldAddRemoveFriend() {
        storage.create(user1);
        storage.create(user2);
        storage.create(user3);

        List<User> etalonFriendsOfUser1 = List.of(user2, user3);
        List<User> etalonFriendsOfUser3 = List.of(user1);

        storage.addFriend(user1.getId(), user2.getId());
        storage.addFriend(user1.getId(), user3.getId());

        jdbcTemplate.update("UPDATE friend SET status = 1 WHERE user2_id = ?", user3.getId());

        List<User> friendsOfUser1 = storage.getFriends(user1.getId());
        assertEquals(etalonFriendsOfUser1, friendsOfUser1, "Список друзей не соответствует добавленному");

        List<User> friendsOfUser2 = storage.getFriends(user2.getId());
        assertEquals(0, friendsOfUser2.size(), "Список друзей не пустой");

        List<User> friendsOfUser3 = storage.getFriends(user3.getId());
        assertEquals(etalonFriendsOfUser3, friendsOfUser3, "Список друзей не соответствует подтвержденному");

        storage.removeFriend(user1.getId(), user3.getId());
        friendsOfUser1 = storage.getFriends(user1.getId());
        friendsOfUser3 = storage.getFriends(user3.getId());

        assertEquals(List.of(user2), friendsOfUser1, "Список друзей после удаления не изменился");
        assertEquals(0, friendsOfUser3.size(), "Список друзей после удаления не опустел");
    }

    @Test
    void shouldGetCommonFriends() {
        storage.create(user1);
        storage.create(user2);
        storage.create(user3);
        storage.create(user4);
        storage.create(user5);

        storage.addFriend(user1.getId(), user2.getId());
        storage.addFriend(user1.getId(), user5.getId());
        storage.addFriend(user2.getId(), user3.getId());
        storage.addFriend(user2.getId(), user4.getId());
        storage.addFriend(user2.getId(), user5.getId());

        List<User> commonFriends12 = storage.getCommonFriends(user1.getId(), user2.getId());
        assertEquals(List.of(user5), commonFriends12, "Список общих друзей некорректный");

        List<User> commonFriends25 = storage.getCommonFriends(user2.getId(), user5.getId());
        assertEquals(0, commonFriends25.size(), "Список общих друзей некорректный при односторонней дружбе");
    }
}
