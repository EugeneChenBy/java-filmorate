package ru.yandex.practicum.filmorate.validation;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmValidationTest {

    @Autowired
    FilmService service;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    Film film;

    private void clearDBData() {
        jdbcTemplate.update("DELETE FROM film");
    }

    @BeforeEach
    void beforeEach() {
        clearDBData();
    }

    void createNewValidFilm() {
        film = Film.builder()
                .name("Форрест Гамп")
                .description("Описание фильма Форрест Гамп 1994 года с Томом Хэнксом в главной роли")
                .releaseDate(LocalDate.of(1994, 06, 23))
                .duration(Duration.ofMinutes(142))
                .mpa(new MPA(1, "G"))
                .genres(new ArrayList<>())
                .build();
    }

    @Test
    void shouldCreateFilm() {
        createNewValidFilm();

        service.create(film);

        assertEquals(1, service.getAllFilms().size(), "Фильм не добавлен");
        assertEquals(film, service.getAllFilms().get(0), "Созданный фильм не равен добавляемому");
    }

    @Test
    void shouldNotCreateFilmBeforeMinimalDate() {
        LocalDate afterMinimal = FilmService.MINIMAL_DATE.plusDays(1);
        LocalDate isMinimal = FilmService.MINIMAL_DATE;
        LocalDate beforeMinimal = FilmService.MINIMAL_DATE.minusDays(1);

        createNewValidFilm();
        film.setReleaseDate(afterMinimal);
        service.create(film);
        assertEquals(1, service.getAllFilms().size(), "Фильм с корректной датой релиза не добавлен");

        clearDBData();
        createNewValidFilm();
        film.setReleaseDate(isMinimal);
        service.create(film);
        assertEquals(1, service.getAllFilms().size(), "Фильм с корректной датой релиза не добавлен");

        clearDBData();
        createNewValidFilm();
        film.setReleaseDate(beforeMinimal);
        try {
            service.create(film);
        } catch (Exception e) {
            assertEquals("Дата выпуска фильма не может быть ранее " + FilmService.MINIMAL_DATE, e.getMessage(), "Ошибка создания фильма со слишком ранней датой релиза не отловлена");
        }
        assertEquals(0, service.getAllFilms().size(), "Фильм с некорректной датой релиза по какой-то причине добавлен");
    }

    @Test
    void shouldNotCreateFilmWithNegativeDuration() {
        Duration durNegative = Duration.ofMinutes(-1);
        Duration durZero = Duration.ofMinutes(0);
        Duration durPositive = Duration.ofMinutes(1);

        createNewValidFilm();
        film.setDuration(durPositive);
        service.create(film);
        assertEquals(1, service.getAllFilms().size(), "Фильм с корректной продолжительностью не добавлен");

        clearDBData();
        createNewValidFilm();
        film.setDuration(durZero);
        service.create(film);
        assertEquals(1, service.getAllFilms().size(), "Фильм с корректной продолжительностью не добавлен");

        clearDBData();
        createNewValidFilm();
        film.setDuration(durNegative);
        try {
            service.create(film);
        } catch (Exception e) {
            assertEquals("Продолжительность фильма не может быть отрицательной", e.getMessage(), "Ошибка создания фильма с отрицательной продолжительностью не отловлена");
        }
        assertEquals(0, service.getAllFilms().size(), "Фильм с некорректной продолжительностью по какой-то причине добавлен");
    }

    @Test
    void shouldNotCreateFilmWithInvalidName() {
        createNewValidFilm();
        film.setName("");
        try {
            service.create(film);
        } catch (Exception e) {
            assertEquals("Наименование фильма не может быть пустым", e.getMessage(), "Ошибка создания фильма с пустым наименованием не отловлена");
        }
        assertEquals(0, service.getAllFilms().size(), "Фильм с пустым наименованием по какой-то причине добавился");

        createNewValidFilm();
        film.setName(null);
        try {
            service.create(film);
        } catch (Exception e) {
            assertEquals("Наименование фильма не может быть пустым", e.getMessage(), "Ошибка создания фильма с пустым наименованием не отловлена");
        }
        assertEquals(0, service.getAllFilms().size(), "Фильм с пустым наименованием по какой-то причине добавился");
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
        service.create(film);
        assertEquals(1, service.getAllFilms().size(), "Фильм с корректным описанием не добавлен");

        clearDBData();
        createNewValidFilm();
        film.setDescription(length200);
        service.create(film);
        assertEquals(1, service.getAllFilms().size(), "Фильм с корректным описанием не добавлен");

        clearDBData();
        createNewValidFilm();
        film.setDescription(length201);
        try {
            service.create(film);
        } catch (Exception e) {
            assertEquals("Максимальная длина описания 200 символов", e.getMessage(), "Ошибка создания фильма с превышенной длиной описания не отловлена");
        }
        assertEquals(0, service.getAllFilms().size(), "Фильм с некорректным описанием по какой-то причине добавлен");
    }
}
