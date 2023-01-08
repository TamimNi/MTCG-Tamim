package org.example.application.game.controller.stats;

import org.example.application.game.model.stats.Stats;
import org.example.application.game.repository.stats.StatsMemoryRepository;
import org.example.application.game.repository.stats.StatsRepository;
import org.example.server.dto.Request;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class StatsControllerTest {
    @Test
    void getStatsForUser() throws SQLException {
        Stats stats = new Stats();
        StatsRepository statsRepository = new StatsMemoryRepository();
        Request request = new Request();
        request.setAuthorization("Basic kienboec-mtcgToken");

        stats = statsRepository.getStatOfUser(request);

        assertEquals(stats.getUsername(), "kienboec");
        assertTrue(stats.getImage() != null);
    }
    @Test
    void statOfUserShouldBeReasonable() throws SQLException {
        Stats stats = new Stats();
        StatsRepository statsRepository = new StatsMemoryRepository();
        Request request = new Request();
        request.setAuthorization("Basic kienboec-mtcgToken");

        stats = statsRepository.getStatOfUser(request);

        assertTrue(stats.getElo() < 11000000 && stats.getElo() > -11000000);
    }
}