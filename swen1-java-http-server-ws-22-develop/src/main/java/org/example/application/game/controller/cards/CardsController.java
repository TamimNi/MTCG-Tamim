package org.example.application.game.controller.cards;

import org.example.application.game.model.cards.Cards;
import org.example.application.game.repository.cards.CardsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.server.dto.Request;
import org.example.server.dto.Response;
import org.example.server.http.ContentType;
import org.example.server.http.Method;
import org.example.server.http.StatusCode;
import java.sql.SQLException;
import java.util.List;

public class CardsController {
    private final CardsRepository cardsRepository;

    public CardsController(CardsRepository cardsRepository) {
        this.cardsRepository = cardsRepository;
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

        //String json = request.getContent();
        List<Cards> cards;
        /*try {
            transactions = objectMapper.readValue(json, transactions.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }*/
        //User user = this.userRepository.register(new User(request.getContent(),request.getPath()));
        cards = cardsRepository.getAquiredCards(request);

        Response response = new Response();
        response.setStatusCode(StatusCode.OK);

        response.setContentType(ContentType.APPLICATION_JSON);
        String content = null;
        try {
            content = objectMapper.writeValueAsString(cards);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        response.setContent(content);

        if(cards.size() == 0) {
            content = "failed";
            response.setStatusCode(StatusCode.BAD_REQUEST);
            response.setContentType(ContentType.TEXT_PLAIN);
            response.setContent(content);
        }
        return response;
    }
}
