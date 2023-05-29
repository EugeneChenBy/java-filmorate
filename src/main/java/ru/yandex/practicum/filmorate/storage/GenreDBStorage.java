package ru.yandex.practicum.filmorate.storage;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.GenreRowMapper;

import java.util.List;

@Repository
public class GenreDBStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Genre> getGenresList() {
        String sql = "SELECT genre_id, name FROM genre;";
        return jdbcTemplate.query(sql, new GenreRowMapper());
    }

    public Genre getGenreById(int id) {
        String sql = "SELECT genre_id, name FROM genre WHERE genre_id = ? ;";
        try {
            return jdbcTemplate.queryForObject(sql, new GenreRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new GenreNotFoundException(String.format("Жанр с id = %d не найден", id));
        }
    }
}
