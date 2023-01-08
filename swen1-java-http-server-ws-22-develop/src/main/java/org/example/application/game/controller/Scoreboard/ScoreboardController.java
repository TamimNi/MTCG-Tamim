package org.example.application.game.controller.Scoreboard;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.application.game.model.scoreboard.Scoreboard;
import org.example.application.game.model.stats.Stats;
import org.example.application.game.repository.scoreboard.ScoreboardRepository;
import org.example.application.game.repository.stats.StatsRepository;
import org.example.server.dto.Request;
import org.example.server.dto.Response;
import org.example.server.http.ContentType;
import org.example.server.http.Method;
import org.example.server.http.StatusCode;

import java.sql.SQLException;


import com.fasterxml.jackson.core.JsonProcessingException;
        import org.example.application.game.repository.stats.*;
        import org.example.application.game.model.stats.*;
        import org.example.server.dto.Request;
        import org.example.server.dto.Response;
        import org.example.server.http.ContentType;
        import org.example.server.http.Method;
        import org.example.server.http.StatusCode;
        import com.fasterxml.jackson.databind.ObjectMapper;

        import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ScoreboardController {
    private final ScoreboardRepository scoreboardRepository;

    public ScoreboardController(ScoreboardRepository scoreboardRepository) {
        this.scoreboardRepository = scoreboardRepository;
    }

    public Response handle(Request request) throws SQLException {
        if (request.getMethod().equals(Method.GET.method)) {
            return create(request);
        }

        Response response = new Response();
        response.setStatusCode(StatusCode.METHODE_NOT_ALLOWED);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setContent(StatusCode.METHODE_NOT_ALLOWED.message);

        return response;
    }

    private Response create(Request request) throws SQLException {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = null;
        //String json = request.getContent();
        List<Scoreboard> scoreboard = new ArrayList<>();
/*            try {
                stats = objectMapper.readValue(json, Stats.class);
            } catch (JsonProcessingException e) {
                System.out.println(" :( " + e);
                throw new RuntimeException(e);
            }
        */

        scoreboard = scoreboardRepository.getScoreboard(request);


        try {
            content = objectMapper.writeValueAsString(scoreboard);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Response response = new Response();
        response.setStatusCode(StatusCode.OK);
        response.setContentType(ContentType.APPLICATION_JSON);
        response.setContent(content);
        return response;
    }
}
