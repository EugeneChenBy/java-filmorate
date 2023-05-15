package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
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
}
