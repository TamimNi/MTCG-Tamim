package org.example.application.game.repository.scoreboard;

import org.example.application.game.model.scoreboard.Scoreboard;
import org.example.application.game.model.stats.Stats;
import org.example.server.dto.Request;

import java.sql.SQLException;

import org.example.application.game.model.stats.*;

        import java.sql.SQLException;
import java.util.List;

import org.example.server.dto.Request;


public interface ScoreboardRepository {
    List<Scoreboard> getScoreboard(Request request) throws SQLException;
}