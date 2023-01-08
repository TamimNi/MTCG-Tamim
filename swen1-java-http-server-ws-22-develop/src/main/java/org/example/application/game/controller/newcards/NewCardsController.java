package org.example.application.game.controller.newcards;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.application.game.model.newcards.NewCards;
import org.example.application.game.model.user.User;
import org.example.application.game.repository.newcards.NewCardsRepository;
import org.example.application.game.repository.user.UserRepository;
import org.example.server.dto.Request;
import org.example.server.dto.Response;
import org.example.server.http.ContentType;
import org.example.server.http.Method;
import org.example.server.http.StatusCode;

import java.sql.SQLException;


import com.fasterxml.jackson.core.JsonProcessingException;
        import org.example.application.game.repository.user.UserRepository;
        import org.example.application.game.model.user.User;
        import org.example.server.dto.Request;
        import org.example.server.dto.Response;
        import org.example.server.http.ContentType;
        import org.example.server.http.Method;
        import org.example.server.http.StatusCode;
        import com.fasterxml.jackson.databind.ObjectMapper;

        import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NewCardsController {
    private final NewCardsRepository newCardsRepository;

    public NewCardsController(NewCardsRepository newCardsRepository) {
        this.newCardsRepository = newCardsRepository;
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

        List<NewCards>newCards = new ArrayList<>();

        newCards = newCardsRepository.getCards(request.getAuthorization());



        try {
            content = objectMapper.writeValueAsString(newCards);
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