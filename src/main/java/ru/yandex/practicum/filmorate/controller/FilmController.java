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

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping("/films")
    public Film create(@RequestBody Film film) {
        log.info("Получен POST-запрос на создание фильма - {}", film);

        return filmService.create(film);
    }

    @PutMapping("/films")
    public Film update(@RequestBody Film film) {
        log.info("Получен PUT-запрос на изменение фильма - {}", film);

        return filmService.update(film);
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
        return filmService.getAllFilms();
    }
}
