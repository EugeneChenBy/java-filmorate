package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserRowMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

@Repository
@Slf4j
public class UserDBStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    private final String userSQL = "SELECT user_id, email, login, name, birthday FROM users";
    private final String friendsSQL = "SELECT user_id, email, login, name, birthday \n" +
                                      "FROM users u INNER JOIN friend f ON u.user_id = f.user2_id \n" +
                                      "WHERE f.user1_id = ? \n" +
                                      "UNION ALL \n" +
                                      "SELECT user_id, email, login, name, birthday \n" +
                                      "FROM users u INNER JOIN friend f ON u.user_id = f.user1_id \n" +
                                      "WHERE f.status = 1 AND f.user2_id = ? ";

    @Autowired
    public UserDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getUsersList() {
        String sql = userSQL + " ORDER BY user_id";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    @Override
    public User getUserById(int id) {
        String sql = userSQL + " WHERE user_id = ? ";

        return jdbcTemplate.queryForObject(sql, new UserRowMapper(), id);
    }

    @Override
    public User create(User user) {
        String sql = "INSERT INTO users (email, login, name, birthday, last_update) " +
                                "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP())";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);

        Integer id = keyHolder.getKey().intValue();
        user.setId(id);

        return user;
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ?, last_update = CURRENT_TIMESTAMP() WHERE user_id = ? ";

        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), Date.valueOf(user.getBirthday()), user.getId());

        return getUserById(user.getId());
    }

    @Override
    public void delete(User user) {
        String sql = "DELETE FROM users WHERE user_id = ?;";

        try {
            jdbcTemplate.update(sql, user.getId());
        } catch (DataAccessException e) {
            throw new UserNotFoundException(String.format("Пользователь не найден %s", user));
        }
    }

    @Override
    public  List<User> getFriends(int id) {
        String sql = friendsSQL + " ORDER BY user_id";
        return jdbcTemplate.query(sql, new UserRowMapper(), id, id);
    }

    @Override
    public List<User> getCommonFriends(int userId, int otherUserId) {
        String sql = "(" + friendsSQL + ")" + "\nINTERSECT\n" + "(" + friendsSQL + ")" + "\nMINUS\n"
                     + userSQL + " WHERE user_id = ?" + "\nMINUS\n" + userSQL + " WHERE user_id = ? ORDER BY user_id";
        return jdbcTemplate.query(sql, new UserRowMapper(), userId, userId, otherUserId, otherUserId, userId, otherUserId);
    }

    @Override
    public void addFriend(int user1Id, int user2Id) {
        String sql = "INSERT INTO friend (user1_id, user2_id, status, last_update) \n" +
                     "VALUES (?, ?, 0, CURRENT_TIMESTAMP())";

        jdbcTemplate.update(sql, user1Id, user2Id);
    }

    @Override
    public void removeFriend(int user1Id, int user2Id) {
        String sqlQuery = "DELETE FROM friend WHERE user1_id IN (?, ?) AND user2_id IN (?, ?) ";

        jdbcTemplate.update(sqlQuery, user1Id, user2Id, user1Id, user2Id);
    }
}
