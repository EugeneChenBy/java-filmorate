package ru.yandex.practicum.filmorate.model.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MPARowMapper implements RowMapper<MPA> {
    @Override
    public MPA mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new MPA(rs.getInt("mpa_id"), rs.getString("name"));
    }
}