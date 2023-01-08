package org.example.application.game.repository.stats;

import org.example.application.game.model.stats.*;

import java.sql.SQLException;

        import org.example.server.dto.Request;


public interface StatsRepository {
    Stats getStatOfUser(Request request) throws SQLException;
}