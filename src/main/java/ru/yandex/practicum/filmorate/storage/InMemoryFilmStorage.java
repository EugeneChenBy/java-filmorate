package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage{
    private Map<Integer, Film> films = new HashMap<>();
    private int id = 0;

    @Override
    public List<Film> getFilmsList() {
        return films.values().stream().collect(Collectors.toList());
    }

    @Override
    public Film create(Film film) {
        id++;
        film.setId(id);
        films.put(film.getId(), film);

        return film;
    }

    @Override
    public Film update(Film film) {
        films.put(film.getId(), film);

        return film;
    }

    @Override
    public void delete(Film film) {
        films.remove(film.getId());
    }

    @Override
    public Film getFilmById(int id) {
        return films.get(id);
    }
}
