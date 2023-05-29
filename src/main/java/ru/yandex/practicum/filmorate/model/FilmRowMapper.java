package ru.yandex.practicum.filmorate.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;

import org.springframework.jdbc.core.RowMapper;

public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Film(rs.getInt("film_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("creation_date").toLocalDate(),
                Duration.ofMinutes(rs.getLong("duration")),
                new MPA(rs.getInt("mpa_id"), rs.getString("mpa_name")),
                rs.getInt("likes"),
                new ArrayList<>()
        );
    }
}