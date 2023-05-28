package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDBStorage;

import java.util.List;

@Service
public class GenreService {
    private final GenreDBStorage genreDBStorage;

    @Autowired
    public GenreService(GenreDBStorage genreDBStorage) {
        this.genreDBStorage = genreDBStorage;
    }

    public List<Genre> getAllGenres() {
        return genreDBStorage.getGenresList();
    };

    public Genre getGenreById(int id) {
        return genreDBStorage.getGenreById(id);
    };
}
