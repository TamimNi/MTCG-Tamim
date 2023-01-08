package org.example.application.game.controller.stats;

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
import java.util.List;

public class StatsController {
    private final StatsRepository statsRepository;

    public StatsController(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
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
        Stats stats = null;
/*            try {
                stats = objectMapper.readValue(json, Stats.class);
            } catch (JsonProcessingException e) {
                System.out.println(" :( " + e);
                throw new RuntimeException(e);
            }
        */

        stats = statsRepository.getStatOfUser(request);


        try {
            content = objectMapper.writeValueAsString(stats);
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

