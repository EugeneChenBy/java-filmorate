package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RestController
@Slf4j
public class GenreController {
    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("/genres")
    public List<Genre> getGenres() {
        log.info("Получен GET-запрос списка всех жанров");

        return genreService.getAllGenres();
    }

    @GetMapping("/genres/{id}")
    public Genre findById(@PathVariable("id") int genreId) {
        log.info("Получен GET-запрос на получение жанра - {}", genreId);

        return genreService.getGenreById(genreId);
    }
}
