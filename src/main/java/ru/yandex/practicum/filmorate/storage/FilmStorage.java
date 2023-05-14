package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.stream.Collectors;

public interface FilmStorage {
    public List<Film> getFilmsList();

    public Film create(Film film);

    public Film update(Film film);

    public void delete(Film film);

    public Film getFilmById(int id);
}
