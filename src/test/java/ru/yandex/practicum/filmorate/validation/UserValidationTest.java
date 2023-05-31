package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserValidationTest {

    @Autowired
    UserService service;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    User user;

    private void clearDBData() {
        jdbcTemplate.update("DELETE FROM users");
    }

    @BeforeEach
    void beforeEach() {
        clearDBData();
    }

    void createNewValidUser() {
        user = User.builder()
                .email("misyuchenko@mail.ru")
                .login("eugene_chen")
                .name("Eugene Chen")
                .birthday(LocalDate.of(1988, 11, 14))
                .build();
    }

    @Test
    void shouldCreateUser() {
        createNewValidUser();

        User userTmp = service.create(user);

        assertEquals(1, service.getAllUsers().size(), "Пользователь не добавлен");
        assertEquals(user, service.getAllUsers().get(0), "Созданный пользователь не равен добавляемому");
    }

    @Test
    void shouldNotCreateWithInvalidBirthday() {
        createNewValidUser();
        user.setBirthday(LocalDate.now().plusDays(1));
        try {
            service.create(user);
        } catch (Exception e) {
            assertEquals("Дата рождения не может быть в будущем", e.getMessage(), "Ошибка создания пользователя с датой рождения в будущем не отловлена");
        }

        clearDBData();
        createNewValidUser();
        user.setBirthday(LocalDate.now());
        service.create(user);
        assertEquals(1, service.getAllUsers().size(), "Пользователь с корректной датой рождения по какой-то причине не добавился");

        clearDBData();
        createNewValidUser();
        user.setBirthday(LocalDate.now().minusDays(1));
        service.create(user);
        assertEquals(1, service.getAllUsers().size(), "Пользователь с корректной датой рождения по какой-то причине не добавился");
    }

    @Test
    void shouldCreateUserWithEmptyName() {
        createNewValidUser();
        user.setName("");
        service.create(user);
        int index = service.getAllUsers().size() - 1;
        assertEquals("eugene_chen", service.getAllUsers().get(index).getName(), "Имя добавленного пользователя не равно логину");

        createNewValidUser();
        user.setName(null);
        service.create(user);
        index = service.getAllUsers().size() - 1;
        assertEquals("eugene_chen", service.getAllUsers().get(index).getName(), "Имя добавленного пользователя не равно логину");
    }

    @Test
    void shouldNotCreateUserWithInvalidLogin() {
        createNewValidUser();
        user.setLogin("");
        try {
            service.create(user);
        } catch (Exception e) {
            assertEquals("Логин не может быть пустым", e.getMessage(), "Ошибка создания пользователя с пустым логином не отловлена");
        }
        assertEquals(0, service.getAllUsers().size(), "Пользователь с инвалидным логином по какой-то причине добавился");

        createNewValidUser();
        user.setLogin(null);
        try {
            service.create(user);
        } catch (Exception e) {
            assertEquals("Логин не может быть пустым", e.getMessage(), "Ошибка создания пользователя с пустым логином не отловлена");
        }
        assertEquals(0, service.getAllUsers().size(), "Пользователь с инвалидным логином по какой-то причине добавился");

        createNewValidUser();
        user.setLogin("eugene chen");
        try {
            service.create(user);
        } catch (Exception e) {
            assertEquals("Логин не может содержать пробелы", e.getMessage(), "Ошибка создания пользователя с пробелами в логине не отловлена");
        }
        assertEquals(0, service.getAllUsers().size(), "Пользователь с инвалидным логином по какой-то причине добавился");
    }

    @Test
    void shouldNotCreateUserWithInvalidEmail() {
        createNewValidUser();
        user.setEmail("");
        try {
            service.create(user);
        } catch (Exception e) {
            assertEquals("Email не может быть пустым", e.getMessage(), "Ошибка создания пользователя с пустым email не отловлена");
        }
        assertEquals(0, service.getAllUsers().size(), "Пользователь с инвалидным Email по какой-то причине добавился");

        createNewValidUser();
        user.setEmail(null);
        try {
            service.create(user);
        } catch (Exception e) {
            assertEquals("Email не может быть пустым", e.getMessage(), "Ошибка создания пользователя с пустым email не отловлена");
        }
        assertEquals(0, service.getAllUsers().size(), "Пользователь с инвалидным Email по какой-то причине добавился");

        createNewValidUser();
        user.setEmail("euegenechen");
        try {
            service.create(user);
        } catch (Exception e) {
            assertEquals("Email должен содержать @", e.getMessage(), "Ошибка создания пользователя без @ в Email не отловлена");
        }
        assertEquals(0, service.getAllUsers().size(), "Пользователь с инвалидным Email по какой-то причине добавился");
    }
}
