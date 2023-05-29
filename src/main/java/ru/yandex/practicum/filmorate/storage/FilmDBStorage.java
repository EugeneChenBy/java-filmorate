package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class FilmDBStorage implements FilmStorage {
    private final String filmSQL = "SELECT f.film_id, f.name, f.description, f.creation_date, f.duration, \n" +
                                   "       m.mpa_id, m.name as mpa_name, \n" +
                                   "       (SELECT COUNT(*) FROM film_like WHERE film_id = f.film_id) likes \n" +
                                   " FROM film f INNER JOIN mpa m ON f.mpa = m.mpa_id";
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public FilmDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private List<Film> loadGenres(List<Film> films) {
        String sqlGenres = "SELECT fg.film_id, fg.genre_id, g.name FROM film_genre fg INNER JOIN genre g ON g.genre_id = fg.genre_id";

        List<Map<String, Object>> genresFilms;

        if (films.size() == 1) {
            sqlGenres = sqlGenres + " WHERE film_id = ?";
            genresFilms = jdbcTemplate.queryForList(sqlGenres, films.get(0).getId());
        } else {
            genresFilms = jdbcTemplate.queryForList(sqlGenres);
        }

        Map<Integer, Film> mapFilms = films.stream().collect(Collectors.toMap(Film::getId, Function.identity()));

        genresFilms.forEach(t -> mapFilms.get(Integer.parseInt(t.get("film_id").toString()))
                        .getGenres().add(new Genre(Integer.parseInt(t.get("genre_id").toString()),
                                          t.get("name").toString())
                                          )
                                );

        return new ArrayList<Film>(mapFilms.values());
    }

    @Override
    public List<Film> getFilmsList() {
        List<Film> films = jdbcTemplate.query(filmSQL, new FilmRowMapper());

        List<Film> genredFilms = loadGenres(films);

        return genredFilms;
    }

    @Override
    public Film getFilmById(int id) {
        String sql = filmSQL + " WHERE film_id = ?";

        Film film = jdbcTemplate.queryForObject(sql, new FilmRowMapper(), id);

        List<Film> filmTmp = new ArrayList<>();
        filmTmp.add(film);

        return loadGenres(filmTmp).get(0);
    }

    private void addFilmGenre(int film_id, List<Genre> genres) {
        if (genres != null && genres.size() > 0) {
            Set<Genre> setGenres = new HashSet<>(genres);
            List<Genre> uniqueGenres = setGenres.stream().collect(Collectors.toList());

            String sqlGenre = "INSERT INTO film_genre (film_id, genre_id, last_update) \n" +
                    "VALUES (?, ?, CURRENT_TIMESTAMP())";
            log.debug(genres.toString());
            for (Genre genre : uniqueGenres) {
                jdbcTemplate.update(sqlGenre,film_id, genre.getId());
            }
        }
    }

    private void deleteFilmGenres(int film_id) {
        String sql = "DELETE FROM film_genre WHERE film_id = ?";

        jdbcTemplate.update(sql, film_id);
    }

    @Override
    public Film create(Film film) {
        String sql = "INSERT INTO film (name, description, creation_date, duration, mpa, last_update) \n" +
                               "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP())";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setLong(4, film.getDuration().toMinutes());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);

        Integer id = keyHolder.getKey().intValue();
        film.setId(id);

        addFilmGenre(id, film.getGenres());

        return film;
    }

    @Override
    public Film update(Film film) {
        String sql = "UPDATE film SET name = ?, description = ?, creation_date = ?, \n" +
                                     "duration = ?, mpa = ?, last_update = CURRENT_TIMESTAMP() WHERE film_id = ?";

        jdbcTemplate.update(sql, film.getName(), film.getDescription(), Date.valueOf(film.getReleaseDate()),
                            film.getDuration().toMinutes(), film.getMpa().getId(), film.getId());

        deleteFilmGenres(film.getId());
        addFilmGenre(film.getId(), film.getGenres());

        return getFilmById(film.getId());
    }

    @Override
    public void delete(Film film) {
        String sql = "DELETE FROM film WHERE film_id = ?";

        jdbcTemplate.update(sql, film.getId());

        deleteFilmGenres(film.getId());
    }

    @Override
    public List<Film> getBestFilms(int size) {
        String sql = filmSQL + "\n ORDER BY likes DESC\nLIMIT ?";

        return jdbcTemplate.query(sql, new FilmRowMapper(), size);
    }

    @Override
    public void addLike(int filmId, int userId) {
        String sql = "INSERT INTO film_like (film_id, user_id, last_update) \n" +
                      "VALUES (?, ?, CURRENT_TIMESTAMP())";

        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        String sqlQuery = "DELETE FROM film_like WHERE film_id = ? AND user_id = ?";

        jdbcTemplate.update(sqlQuery, filmId, userId);
    }
}
