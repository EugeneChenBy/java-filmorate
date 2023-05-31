package ru.yandex.practicum.filmorate.storage;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.MPANotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.MPARowMapper;

import java.util.List;

@Repository
public class MPADBStorage {
    private final JdbcTemplate jdbcTemplate;

    public MPADBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<MPA> getMPAsList() {
        String sql = "SELECT mpa_id, name FROM mpa ORDER BY mpa_id";
        return jdbcTemplate.query(sql, new MPARowMapper());
    }

    public MPA getMPAById(int id) {
        String sql = "SELECT mpa_id, name FROM mpa WHERE mpa_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new MPARowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new MPANotFoundException(String.format("MPA-рейтинг с id = %d не найден", id));
        }
    }
}
