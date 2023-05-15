package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    FilmController controller;
    Film film;
/*

    @BeforeEach
    void beforeEach() {
        controller = new FilmController();
    }

    void createNewValidFilm() {
        film = Film.builder()
                .name("Форрест Гамп")
                .description("Описание фильма Форрест Гамп 1994 года с Томом Хэнксом в главной роли")
                .releaseDate(LocalDate.of(1994, 06, 23))
                .duration(Duration.ofMinutes(142))
                .build();
    }

    @Test
    void shouldCreateFilm() {
        controller = new FilmController();

        createNewValidFilm();

        controller.create(film);

        assertEquals(1, controller.getFilms().size(), "Фильм не добавлен");
        assertEquals(film, controller.getFilms().get(0), "Созданный фильм не равен добавляемому");
    }

    @Test
    void shouldNotCreateFilmBeforeMinimalDate() {
        LocalDate afterMinimal = FilmController.MINIMAL_DATE.plusDays(1);
        LocalDate isMinimal = FilmController.MINIMAL_DATE;
        LocalDate beforeMinimal = FilmController.MINIMAL_DATE.minusDays(1);

        createNewValidFilm();
        film.setReleaseDate(afterMinimal);
        controller.create(film);
        assertEquals(1, controller.getFilms().size(), "Фильм с корректной датой релиза не добавлен");

        controller = new FilmController();
        createNewValidFilm();
        film.setReleaseDate(isMinimal);
        controller.create(film);
        assertEquals(1, controller.getFilms().size(), "Фильм с корректной датой релиза не добавлен");

        controller = new FilmController();
        createNewValidFilm();
        film.setReleaseDate(beforeMinimal);
        try {
            controller.create(film);
        } catch (Exception e) {
            assertEquals("Дата выпуска фильма не может быть ранее " + FilmController.MINIMAL_DATE, e.getMessage(), "Ошибка создания фильма со слишком ранней датой релиза не отловлена");
        }
        assertEquals(0, controller.getFilms().size(), "Фильм с некорректной датой релиза по какой-то причине добавлен");
    }

    @Test
    void shouldNotCreateFilmWithNegativeDuration() {
        Duration durNegative = Duration.ofMinutes(-1);
        Duration durZero = Duration.ofMinutes(0);
        Duration durPositive = Duration.ofMinutes(1);

        createNewValidFilm();
        film.setDuration(durPositive);
        controller.create(film);
        assertEquals(1, controller.getFilms().size(), "Фильм с корректной продолжительностью не добавлен");

        controller = new FilmController();
        createNewValidFilm();
        film.setDuration(durZero);
        controller.create(film);
        assertEquals(1, controller.getFilms().size(), "Фильм с корректной продолжительностью не добавлен");

        controller = new FilmController();
        createNewValidFilm();
        film.setDuration(durNegative);
        try {
            controller.create(film);
        } catch (Exception e) {
            assertEquals("Продолжительность фильма не может быть отрицательной", e.getMessage(), "Ошибка создания фильма с отрицательной продолжительностью не отловлена");
        }
        assertEquals(0, controller.getFilms().size(), "Фильм с некорректной продолжительностью по какой-то причине добавлен");
    }

    @Test
    void shouldNotCreateFilmWithInvalidName() {
        createNewValidFilm();
        film.setName("");
        try {
            controller.create(film);
        } catch (Exception e) {
            assertEquals("Наименование фильма не может быть пустым", e.getMessage(), "Ошибка создания фильма с пустым наименованием не отловлена");
        }
        assertEquals(0, controller.getFilms().size(), "Фильм с пустым наименованием по какой-то причине добавился");

        createNewValidFilm();
        film.setName(null);
        try {
            controller.create(film);
        } catch (Exception e) {
            assertEquals("Наименование фильма не может быть пустым", e.getMessage(), "Ошибка создания фильма с пустым наименованием не отловлена");
        }
        assertEquals(0, controller.getFilms().size(), "Фильм с пустым наименованием по какой-то причине добавился");
    }

    @Test
    void shouldAddDescriptionLength200() {
        String length199 = null;
        String length200 = null;
        String length201 = null;

        StringBuilder builder = new StringBuilder();

        for (int i = 1; i <= 201; i++) {
            builder.append("1");
            if (i == 199) {
                length199 = builder.toString();
            } else if (i == 200) {
                length200 = builder.toString();
            } else if (i == 201) {
                length201 = builder.toString();
            }
        }

        createNewValidFilm();
        film.setDescription(length199);
        controller.create(film);
        assertEquals(1, controller.getFilms().size(), "Фильм с корректным описанием не добавлен");

        controller = new FilmController();
        createNewValidFilm();
        film.setDescription(length200);
        controller.create(film);
        assertEquals(1, controller.getFilms().size(), "Фильм с корректным описанием не добавлен");

        controller = new FilmController();
        createNewValidFilm();
        film.setDescription(length201);
        try {
            controller.create(film);
        } catch (Exception e) {
            assertEquals("Максимальная длина описания 200 символов", e.getMessage(), "Ошибка создания фильма с превышенной длиной описания не отловлена");
        }
        assertEquals(0, controller.getFilms().size(), "Фильм с некорректным описанием по какой-то причине добавлен");
    }

 */
}
