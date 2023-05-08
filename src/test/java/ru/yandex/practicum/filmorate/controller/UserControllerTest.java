package ru.yandex.practicum.filmorate.controller;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    UserController controller;
    User user;


    @BeforeEach
    void BeforeEach() {
        controller = new UserController();
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
        controller = new UserController();

        createNewValidUser();

        controller.create(user);

        assertEquals(1, controller.getUsers().size(), "Пользователь не добавлен");
        assertEquals(user, controller.getUsers().get(0), "Созданный пользователь не равен добавляемому");
    }

    @Test
    void shouldNotCreateWithInvalidBirthday() {
        createNewValidUser();
        user.setBirthday(LocalDate.now().plusDays(1));
        try {
            controller.create(user);
        } catch (Exception e) {
            assertEquals("Дата рождения не может быть в будущем", e.getMessage(), "Ошибка создания пользователя с датой рождения в будущем не отловлена");
        }

        controller = new UserController();
        createNewValidUser();
        user.setBirthday(LocalDate.now());
        controller.create(user);
        assertEquals(1, controller.getUsers().size(), "Пользователь с корректной датой рождения по какой-то причине добавился");

        controller = new UserController();
        createNewValidUser();
        user.setBirthday(LocalDate.now().minusDays(1));
        controller.create(user);
        assertEquals(1, controller.getUsers().size(), "Пользователь с корректной датой рождения по какой-то причине добавился");
    }

    @Test
    void shouldCreateUserWithEmptyName() {
        createNewValidUser();
        user.setName("");
        controller.create(user);
        int index = controller.getUsers().size() - 1;
        assertEquals("eugene_chen", controller.getUsers().get(index).getName(), "Имя добавленного пользователя не равно логину");

        createNewValidUser();
        user.setName(null);
        controller.create(user);
        index = controller.getUsers().size() - 1;
        assertEquals("eugene_chen", controller.getUsers().get(index).getName(), "Имя добавленного пользователя не равно логину");
    }

    @Test
    void shouldNotCreateUserWithInvalidLogin() {
        createNewValidUser();
        user.setLogin("");
        try {
            controller.create(user);
        } catch (Exception e) {
            assertEquals("Логин не может быть пустым", e.getMessage(), "Ошибка создания пользователя с пустым логином не отловлена");
        }
        assertEquals(0, controller.getUsers().size(), "Пользователь с инвалидным логином по какой-то причине добавился");

        createNewValidUser();
        user.setLogin(null);
        try {
            controller.create(user);
        } catch (Exception e) {
            assertEquals("Логин не может быть пустым", e.getMessage(), "Ошибка создания пользователя с пустым логином не отловлена");
        }
        assertEquals(0, controller.getUsers().size(), "Пользователь с инвалидным логином по какой-то причине добавился");

        createNewValidUser();
        user.setLogin("eugene chen");
        try {
            controller.create(user);
        } catch (Exception e) {
            assertEquals("Логин не может содержать пробелы", e.getMessage(), "Ошибка создания пользователя с пробелами в логине не отловлена");
        }
        assertEquals(0, controller.getUsers().size(), "Пользователь с инвалидным логином по какой-то причине добавился");
    }

    @Test
    void shouldNotCreateUserWithInvalidEmail() {
        createNewValidUser();
        user.setEmail("");
        try {
            controller.create(user);
        } catch (Exception e) {
            assertEquals("Email не может быть пустым", e.getMessage(), "Ошибка создания пользователя с пустым email не отловлена");
        }
        assertEquals(0, controller.getUsers().size(), "Пользователь с инвалидным Email по какой-то причине добавился");

        createNewValidUser();
        user.setEmail(null);
        try {
            controller.create(user);
        } catch (Exception e) {
            assertEquals("Email не может быть пустым", e.getMessage(), "Ошибка создания пользователя с пустым email не отловлена");
        }
        assertEquals(0, controller.getUsers().size(), "Пользователь с инвалидным Email по какой-то причине добавился");

        createNewValidUser();
        user.setEmail("euegenechen");
        try {
            controller.create(user);
        } catch (Exception e) {
            assertEquals("Email должен содержать @", e.getMessage(), "Ошибка создания пользователя без @ в Email не отловлена");
        }
        assertEquals(0, controller.getUsers().size(), "Пользователь с инвалидным Email по какой-то причине добавился");
    }
}