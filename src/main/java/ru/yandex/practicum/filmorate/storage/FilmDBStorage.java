package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.rowmapper.FilmRowMapper;

import java.util.List;

@Repository
public class FilmDBStorage implements FilmStorage{
    private final String filmSQL = "SELECT film_id, name, description, creation_date, duration, mpa FROM film";
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public FilmDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> getFilmsList() {
        String sql = filmSQL + " ;";
        return jdbcTemplate.query(sql, new FilmRowMapper());
    }

    @Override
    public Film getFilmById(int id) {
        String sql = filmSQL + " WHERE film_id = ? ;";

        return jdbcTemplate.queryForObject(sql, new FilmRowMapper(), id);
    }

    @Override
    public Film create(Film film) {
        String sql = "INSERT INTO film (name, description, creation_date, duration, mpa, last_update) \n" +
                               "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP());";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(sql, keyHolder);

        Integer id = keyHolder.getKey().intValue();
        film.setId(id);

        return film;
    }

    @Override
    public Film update(Film film) {
        String sql = "UPDATE film SET name = ?, description = ?, creation_date = ?, \n" +
                                     "duration = ?, mpa = ?, last_update = CURRENT_TIMESTAMP()) WHERE film_id = ?;";

        try {
            jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                                     film.getDuration().toMinutes(), film.getMpa().getId(), film.getId());
        } catch (DataAccessException e) {
            throw new FilmNotFoundException(String.format("Фильм не найден %s", film));
        }

        return getFilmById(film.getId());
    }

    @Override
    public void delete(Film film) {
        String sql = "DELETE FROM film WHERE film_id = ?;";

        try {
            jdbcTemplate.update(sql, film.getId());
        } catch (DataAccessException e) {
            throw new FilmNotFoundException(String.format("Фильм не найден %s", film));
        }
    }

    @Override
    public List<Film> getBestFilms(int size) {
        String sql = "SELECT film_id, name, description, creation_date, duration, mpa,\n" +
                     "       (SELECT COUNT(*) FROM film_like WHERE film_id = f.film_id) cnt\n" +
                     "FROM film f\n" +
                     "ORDER BY cnt DESC\n" +
                     "LIMIT ? ;";
        try {
            return jdbcTemplate.query(sql, new FilmRowMapper(), size);
        } catch (EmptyResultDataAccessException e) {
            throw new FilmNotFoundException("Что-то пошло не так при запросе лучших фильмов");
        }
    }

    @Override
    public void addLike(int film_id, int user_id) {
        String sql = "INSERT INTO film_like (film_id, user_id, last_update) \n" +
                      "VALUES (?, ?, SYSTIMESTAMP);";

        jdbcTemplate.update(sql, film_id, user_id);
    }

    @Override
    public void removeLike(int film_id, int user_id) {
        String sqlQuery = "DELETE FROM film_like WHERE film_id = ? AND user_id = ? ;";

        jdbcTemplate.update(sqlQuery, film_id, user_id);
    }
}
