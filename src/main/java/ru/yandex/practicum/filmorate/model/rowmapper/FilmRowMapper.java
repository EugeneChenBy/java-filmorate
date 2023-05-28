package ru.yandex.practicum.filmorate.model.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;

public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Film(rs.getInt("film_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("creation_date").toLocalDate(),
                Duration.ofMinutes(rs.getLong("duration")),
                new MPA(rs.getInt("mpa"))
        );
    }
}