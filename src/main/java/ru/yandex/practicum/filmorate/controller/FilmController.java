package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class FilmController {
    private final FilmService filmService;
    public static final LocalDate MINIMAL_DATE = LocalDate.of(1895, 12, 25);
    private int id = 0;
    private Map<Integer, Film> films = new HashMap<>();

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping("/films")
    public Film create(@RequestBody Film film) {
        log.info("Получен POST-запрос на создание фильма - {}", film);

        try {
            validate(film);
        } catch (ValidationException e) {
            log.error(e.getMessage());
            throw new ValidationException(e.getMessage());
        }

        id++;
        film.setId(id);
        films.put(film.getId(), film);

        return film;
    }

    @PutMapping("/films")
    public Film update(@RequestBody Film film) {
        log.info("Получен PUT-запрос на изменение фильма - {}", film);

        try {
            validate(film);
        } catch (ValidationException e) {
            log.error(e.getMessage());
            throw new ValidationException(e.getMessage());
        }

        if (!films.containsKey(film.getId())) {
            String text = "Не найден фильм с id = " + film.getId();
            log.error(text);
            throw new ValidationException(text);
        }
        films.put(film.getId(), film);

        return film;
    }

    @GetMapping("/films/{id}")
    public Film findById(@PathVariable("id") int filmId) {
        return filmService.getFilmById(filmId);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void likeFilm(@PathVariable("id") int filmId, @PathVariable("userId") int userId) {
        filmService.likeFilm(filmId, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void removeLikeFilm(@PathVariable("id") int filmId, @PathVariable("userId") int userId) {
        filmService.removeLike(filmId, userId);
    }

    @GetMapping("/films/popular?count={count}")
    public List<Film> getBestFilms(@RequestParam(name = "count", defaultValue = "10") int count) {
        return getBestFilms(count);
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        return films.values().stream().collect(Collectors.toList());
    }

    private void validate(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Наименование фильма не может быть пустым");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания 200 символов");
        }
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(MINIMAL_DATE)) {
            throw new ValidationException("Дата выпуска фильма не может быть ранее " + MINIMAL_DATE);
        }
        if (film.getDuration() != null && film.getDuration().isNegative()) {
            throw new ValidationException("Продолжительность фильма не может быть отрицательной");
        }
    }
}
