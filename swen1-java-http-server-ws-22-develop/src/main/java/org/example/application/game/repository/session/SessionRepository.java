package org.example.application.game.repository.session;

import org.example.application.game.model.session.Session;
import java.sql.SQLException;

public interface SessionRepository {
    Session verify(Session session) throws SQLException;
}
