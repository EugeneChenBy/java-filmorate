package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class FilmController {
    public static final LocalDate MINIMAL_DATE = LocalDate.of(1895, 12, 25);
    private int id = 0;
    private Map<Integer, Film> films = new HashMap<>();

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
