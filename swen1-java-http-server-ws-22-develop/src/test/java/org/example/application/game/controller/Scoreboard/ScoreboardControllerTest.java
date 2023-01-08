package org.example.application.game.controller.Scoreboard;

import org.example.application.game.model.scoreboard.Scoreboard;
import org.example.application.game.repository.scoreboard.ScoreboardMemoryRepository;
import org.example.application.game.repository.scoreboard.ScoreboardRepository;
import org.example.server.dto.Request;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScoreboardControllerTest {
    @Test
    void getScore() throws SQLException {
        List<Scoreboard> scoreboard = new ArrayList<>();
        ScoreboardRepository scoreboardRepository = new ScoreboardMemoryRepository();
        Request request = new Request();
        request.setAuthorization("Basic altenhof-mtcgToken");

        scoreboard = scoreboardRepository.getScoreboard(request);

        assertTrue(scoreboard.size() > 0);
    }
    @Test
    void getScoreSameForAnyUser() throws SQLException {
        List<Scoreboard> scoreboard = new ArrayList<>();
        List<Scoreboard> scoreboard2 = new ArrayList<>();
        ScoreboardRepository scoreboardRepository = new ScoreboardMemoryRepository();
        Request request = new Request();
        request.setAuthorization("Basic altenhof-mtcgToken");
        Request request2 = new Request();
        request2.setAuthorization("Basic kienboec-mtcgToken");

        scoreboard = scoreboardRepository.getScoreboard(request);
        scoreboard2 = scoreboardRepository.getScoreboard(request2);

        assertEquals(scoreboard.size(),scoreboard2.size());
        assertTrue(scoreboard.get(1).getElo() == scoreboard2.get(1).getElo());
    }
    @Test
    void getScoreWrongToken() throws SQLException {
        List<Scoreboard> scoreboard = new ArrayList<>();
        ScoreboardRepository scoreboardRepository = new ScoreboardMemoryRepository();
        Request request = new Request();
        request.setAuthorization("Basic wrong-mtcgToken");

        scoreboard = scoreboardRepository.getScoreboard(request);

        assertTrue(scoreboard.size() == 0);
    }

    @Test
    void scoreNotChangingCalledBySameUser() throws SQLException {
        List<Scoreboard> scoreboard = new ArrayList<>();
        List<Scoreboard> scoreboard2 = new ArrayList<>();
        ScoreboardRepository scoreboardRepository = new ScoreboardMemoryRepository();
        Request request = new Request();
        request.setAuthorization("Basic altenhof-mtcgToken");

        scoreboard = scoreboardRepository.getScoreboard(request);
        scoreboard2 = scoreboardRepository.getScoreboard(request);

        assertTrue(scoreboard.size() == scoreboard2.size());
        assertTrue(scoreboard.get(1).getElo() == scoreboard2.get(1).getElo());
    }
    @Test
    void scoreHighestToLowest() throws SQLException {
        List<Scoreboard> scoreboard = new ArrayList<>();
        ScoreboardRepository scoreboardRepository = new ScoreboardMemoryRepository();
        Request request = new Request();
        request.setAuthorization("Basic altenhof-mtcgToken");

        scoreboard = scoreboardRepository.getScoreboard(request);

        assertTrue(scoreboard.get(1).getElo() < scoreboard.get(0).getElo());
    }
}