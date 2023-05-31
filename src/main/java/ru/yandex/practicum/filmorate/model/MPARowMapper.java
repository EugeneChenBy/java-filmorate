package ru.yandex.practicum.filmorate.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MPARowMapper implements RowMapper<MPA> {
    @Override
    public MPA mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new MPA(rs.getInt("mpa_id"), rs.getString("name"));
    }
}