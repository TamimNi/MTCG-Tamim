package org.example.application.game.repository.user;

import org.example.application.game.model.user.User;
import org.example.server.dto.Request;

import java.sql.SQLException;

public interface UserRepository {
    User save(User user) throws SQLException;

    User getUser(String token, String username) throws SQLException;

    User editUser(String token, String username, User user) throws SQLException;
}
