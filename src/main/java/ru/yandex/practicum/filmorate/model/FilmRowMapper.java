package ru.yandex.practicum.filmorate.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        List<Genre> genres = new ArrayList<>();
        int genreId = rs.getInt("genre_id");
        if (genreId > 0) {
            Genre genre = new Genre(rs.getInt("genre_id"), rs.getString("genre_name"));
            genres = new ArrayList<>(List.of(genre));
        }

        return new Film(rs.getInt("film_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("creation_date").toLocalDate(),
                Duration.ofMinutes(rs.getLong("duration")),
                new MPA(rs.getInt("mpa_id"), rs.getString("mpa_name")),
                rs.getInt("likes"),
                genres
        );
    }
}