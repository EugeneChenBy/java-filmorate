package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

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
        log.info("Получен GET-запрос на получение фильма - {}", filmId);

        return filmService.getFilmByIdElseThrow(filmId);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void likeFilm(@PathVariable("id") int filmId, @PathVariable("userId") int userId) {
        log.info("Получен PUT-запрос на установку лайка фильму - {} от пользователя - {}", filmId, userId);

        filmService.likeFilm(filmId, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void removeLikeFilm(@PathVariable("id") int filmId, @PathVariable("userId") int userId) {
        log.info("Получен DELETE-запрос на снятие лайка с фильма - {} от пользователя - {}", filmId, userId);

        filmService.removeLike(filmId, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getBestFilms(@RequestParam(name = "count", defaultValue = "10", required = false) int count) {
        log.info("Получен GET-запрос списка {} популярных фильмов", count);

        return filmService.getBestFilms(count);
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        log.info("Получен GET-запрос списка всех фильмов");

        return filmService.getAllFilms();
    }
}
