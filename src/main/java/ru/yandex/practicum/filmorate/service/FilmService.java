package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    public static final LocalDate MINIMAL_DATE = LocalDate.of(1895, 12, 25);
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film create( Film film) {
        try {
            validate(film);
        } catch (ValidationException e) {
            log.error(e.getMessage());
            throw new ValidationException(e.getMessage());
        }

        return filmStorage.create(film);
    }

    public Film update( Film film) {
        try {
            validate(film);
        } catch (ValidationException e) {
            log.error(e.getMessage());
            throw new ValidationException(e.getMessage());
        }

        if (filmStorage.getFilmById(film.getId()) == null) {
            String text = "Не найден фильм с id = " + film.getId();
            log.error(text);
            throw new ValidationException(text);
        }
        return filmStorage.update(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getFilmsList();
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }

    public void likeFilm(int filmId, int userId) {
        Film film = getFilmById(filmId);
        User user = userStorage.getUserById(userId);

        likeFilm(film, user);
    }

    public void likeFilm(Film film, User user) {
        Set<Integer> likes = film.getLikes();
        likes.add(user.getId());
        film.setLikes(likes);

        filmStorage.update(film);
    }

    public void removeLike(int filmId, int userId) {
        Film film = getFilmById(filmId);
        User user = userStorage.getUserById(userId);

        removeLike(film, user);
    }

    public void removeLike(Film film, User user) {
        Set<Integer> likes = film.getLikes();
        likes.remove(user.getId());
        film.setLikes(likes);

        filmStorage.update(film);
    }

    public List<Film> getBestFilms(int size) {
        return filmStorage.getFilmsList().stream()
                .sorted((f0, f1) -> compare(f0, f1))
                .limit(size)
                .collect(Collectors.toList());
    }

    private int compare(Film f0, Film f1) {
        return f0.getLikes().size() - f1.getLikes().size();
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
