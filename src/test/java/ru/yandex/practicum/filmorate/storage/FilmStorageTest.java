package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmStorageTest {
    @Autowired
    UserStorage userStorage;
    @Autowired
    FilmService service;
    @Autowired
    JdbcTemplate jdbcTemplate;

    List<Film> films;
    Film film1;
    Film film2;
    Film film3;
    Film film4;
    Film film5;

    List<User> users;
    static User user1;
    static User user2;
    static User user3;
    static User user4;
    static User user5;

    @BeforeAll
    static void beforeAll() {
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
    private void renewData() {
        jdbcTemplate.update("DELETE FROM film_like");
        jdbcTemplate.update("DELETE FROM film_genre");
        jdbcTemplate.update("DELETE FROM film");
        jdbcTemplate.update("DELETE FROM users");

        films = new ArrayList<>();

        film1 = Film.builder()
                .name("Форрест Гамп")
                .description("Описание фильма Форрест Гамп 1994 года с Томом Хэнксом в главной роли")
                .releaseDate(LocalDate.of(1994, 06, 23))
                .duration(Duration.ofMinutes(142))
                .mpa(new MPA(1, "G"))
                .genres(List.of(new Genre(1, "Комедия"), new Genre(2, "Драма")))
                .build();

        film2 = Film.builder()
                .name("Зеленая миля")
                .description("Описание фильма Зеленая миля 1998 года с Томом Хэнксом в главной роли")
                .releaseDate(LocalDate.of(1997, 02, 10))
                .duration(Duration.ofMinutes(180))
                .mpa(new MPA(3, "PG-13"))
                .genres(List.of(new Genre(2, "Драма"), new Genre(4, "Триллер")))
                .build();

        film3 = Film.builder()
                .name("Миссия невыполнима 4")
                .description("Описание фильма Mission Impossible 2006 года с Томом Крузом в главной роли")
                .releaseDate(LocalDate.of(1994, 06, 23))
                .duration(Duration.ofMinutes(118))
                .mpa(new MPA(2, "PG"))
                .genres(List.of(new Genre(4, "Триллер"), new Genre(6, "Боевик")))
                .build();

        film4 = Film.builder()
                .name("Король Лев")
                .description("Описание мультфильма Король Лев 1994 года с Симбой, Тимоном и Пумбой в главных ролях")
                .releaseDate(LocalDate.of(1994, 06, 23))
                .duration(Duration.ofMinutes(115))
                .mpa(new MPA(5, "NC-17"))
                .genres(List.of(new Genre(2, "Драма"), new Genre(3, "Мультфильм")))
                .build();

        film5 = Film.builder()
                .name("Джентельмены")
                .description("Описание фильма Джентельмены 2020 года с Мэттью МакКонахи в главной роли")
                .releaseDate(LocalDate.of(2020, 3, 8))
                .duration(Duration.ofMinutes(130))
                .mpa(new MPA(4, "R"))
                .genres(List.of(new Genre(4, "Триллер"), new Genre(6, "Боевик")))
                .build();
    }

    @BeforeEach
    void beforeEach() {
        renewData();
    }

    @Test
    void shouildGetFilmsList(){
        films = List.of(film1, film2, film3, film4, film5);

        service.create(film1);
        service.create(film2);
        service.create(film3);
        service.create(film4);
        service.create(film5);

        List<Film> real = service.getAllFilms();

        assertEquals(films, real, "Полученный список фильмов не равен добавленному и хранимому в БД");
    }

    @Test
    void shouldCreateFilm() {
        Film filmAdded = service.create(film1);

        assertEquals(film1, filmAdded, "Добавленный фильм не равен добавляемому");
    }

    @Test
    void shouldUpdateFilm() {
        service.create(film1);

        film1.setDescription("Фильм Гая Риччи");
        service.update(film1);

        Film filmrUpdated = service.getFilmByIdElseThrow(film1.getId());

        assertEquals(film1, filmrUpdated, "Фильм не обновился");
    }

    @Test
    void shouldGetFilmById() {
        Film film1Added = service.create(film1);

        Film film1Check = service.getFilmByIdElseThrow(film1.getId());
        assertEquals(film1, film1Check, "Добавленный фильм не равен найденному по id");
    }

    @Test
    void shouldThrowGetFilmByWrongId() {
        assertThrows(FilmNotFoundException.class, () -> service.getFilmByIdElseThrow(50));
    }

    @Test
    void shouldGetBestFilms() {
        userStorage.create(user1);
        userStorage.create(user2);
        userStorage.create(user3);
        userStorage.create(user4);
        userStorage.create(user5);

        service.create(film1);
        service.create(film2);
        service.create(film3);
        service.create(film4);
        service.create(film5);

        service.likeFilm(film2.getId(), user1.getId());
        service.likeFilm(film2.getId(), user2.getId());
        service.likeFilm(film2.getId(), user3.getId());
        service.likeFilm(film2.getId(), user4.getId());
        service.likeFilm(film2.getId(), user5.getId());
        film2 = service.getFilmByIdElseThrow(film2.getId());
        assertEquals(5, film2.getLikes(), "Количество лайков неверное film2");

        service.likeFilm(film1.getId(), user1.getId());
        service.likeFilm(film1.getId(), user2.getId());
        service.likeFilm(film1.getId(), user3.getId());
        service.likeFilm(film1.getId(), user4.getId());
        film1 = service.getFilmByIdElseThrow(film1.getId());
        assertEquals(4, film1.getLikes(), "Количество лайков неверное film1");

        service.likeFilm(film4.getId(), user1.getId());
        service.likeFilm(film4.getId(), user2.getId());
        service.likeFilm(film4.getId(), user3.getId());
        film4 = service.getFilmByIdElseThrow(film4.getId());
        assertEquals(3, film4.getLikes(), "Количество лайков неверное film4");

        service.likeFilm(film5.getId(), user3.getId());
        service.likeFilm(film5.getId(), user4.getId());
        film5 = service.getFilmByIdElseThrow(film5.getId());
        assertEquals(2, film5.getLikes(), "Количество лайков неверное film5");

        service.likeFilm(film3.getId(), user5.getId());
        film3 = service.getFilmByIdElseThrow(film3.getId());
        assertEquals(1, film3.getLikes(), "Количество лайков неверное film3");

        List<Film> etalonBest = List.of(film2, film1, film4, film5, film3);
        List<Film> realBest = service.getBestFilms(10);

        assertEquals(etalonBest, realBest, "Список лучших фильмов не равен ожидаемому в запросе по умолчанию");

        service.removeLike(film1.getId(), user1.getId());
        service.removeLike(film1.getId(), user2.getId());
        service.likeFilm(film4.getId(), user4.getId());
        service.likeFilm(film5.getId(), user5.getId());
        film1 = service.getFilmByIdElseThrow(film1.getId());
        film4 = service.getFilmByIdElseThrow(film4.getId());
        film5 = service.getFilmByIdElseThrow(film5.getId());
        etalonBest = List.of(film2, film4, film5, film1, film3);
        realBest = service.getBestFilms(10);

        assertEquals(etalonBest, realBest, "Список лучших фильмов не равен ожидаемому в запросе по умолчанию после удаления и добавления лайков");

        etalonBest = List.of(film2, film4, film5);
        realBest = service.getBestFilms(3);

        assertEquals(etalonBest, realBest, "Список 3 лучших фильмов не равен ожидаемому");
    }
}
