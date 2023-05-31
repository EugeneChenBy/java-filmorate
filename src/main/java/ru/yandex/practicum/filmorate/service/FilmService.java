package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.List;

@Service
public class FilmService {
    public static final LocalDate MINIMAL_DATE = LocalDate.of(1895, 12, 25);
    private final FilmStorage filmStorage;
    private final UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Film getFilmByIdElseThrow(int id) {
        try {
            return filmStorage.getFilmById(id);
        } catch (IndexOutOfBoundsException e) {
            String text = "Не найден фильм с id = " + id;
            throw new FilmNotFoundException(text);
        }
    }

    public Film create(Film film) {
        validate(film);

        film.setLikes(0);

        return filmStorage.create(film);
    }

    public Film update(Film film) {
        validate(film);

        Film filmTmp = getFilmByIdElseThrow(film.getId());

        return filmStorage.update(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getFilmsList();
    }

    public void likeFilm(int filmId, int userId) {
        Film film = getFilmByIdElseThrow(filmId);
        User user = userService.getUserByIdElseThrow(userId);

        likeFilm(film, user);
    }

    private void likeFilm(Film film, User user) {
        filmStorage.addLike(film.getId(), user.getId());
    }

    public void removeLike(int filmId, int userId) {
        Film film = getFilmByIdElseThrow(filmId);
        User user = userService.getUserByIdElseThrow(userId);

        removeLike(film, user);
    }

    private void removeLike(Film film, User user) {
        filmStorage.removeLike(film.getId(),user.getId());
    }

    public List<Film> getBestFilms(int size) {
        return filmStorage.getBestFilms(size);
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
