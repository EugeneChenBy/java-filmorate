package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    public List<Film> getFilmsList();

    public Film create(Film film);

    public Film update(Film film);

    public void delete(Film film);

    public Film getFilmById(int id);

    public List<Film> getBestFilms(int size);
}
