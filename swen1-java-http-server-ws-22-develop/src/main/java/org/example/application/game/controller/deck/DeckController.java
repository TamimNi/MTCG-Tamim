package org.example.application.game.controller.deck;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.application.game.model.deck.Deck;
import org.example.application.game.model.packages.Packages;
import org.example.application.game.repository.deck.DeckRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.server.dto.Request;
import org.example.server.dto.Response;
import org.example.server.http.ContentType;
import org.example.server.http.Method;
import org.example.server.http.StatusCode;
import java.sql.SQLException;
import java.util.List;

public class DeckController {
    private final DeckRepository deckRepository;
    boolean returnPlain = false;
    public DeckController(DeckRepository deckRepository) {
        this.deckRepository = deckRepository;
    }

    public Response handle(Request request) throws SQLException {
        if (request.getMethod().equals(Method.GET.method) || request.getMethod().equals(Method.PUT.method)) {
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
        String json = request.getContent();
        List<Deck> decks;
        boolean putting = false;
        if(request.getMethod().equals(Method.PUT.method)) {
            //System.out.println("\n\nhier\n"+request.getPath()+"\n"+request.getMethod()+"\n"+returnPlain);
            try {
                decks = objectMapper.readValue(json, new TypeReference<List<Deck>>() {
                });
            } catch (JsonProcessingException e) {
                System.out.println("das " + e + "\n" + request.getMethod());
                throw new RuntimeException(e);
            }
                decks = deckRepository.chooseDeckCards(decks, request);
            putting = true;
        }
        else{ //GET
            if(request.getPath().equals("/deck")){
                returnPlain = false;
            }
            else if(request.getPath().equals("/deck?format=plain")){
                returnPlain = true;
            }
                decks = deckRepository.getDeckCards(request);
        }

        Response response = new Response();
        response.setStatusCode(StatusCode.OK);
        response.setContentType(ContentType.APPLICATION_JSON);
        String content = null;

        if(!returnPlain) {
            try {
                content = objectMapper.writeValueAsString(decks);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }else{
            StringBuilder contentPlain = new StringBuilder();
            for(Deck dck : decks){
                contentPlain.append(dck);
                content = contentPlain.toString();
            }
            response.setContentType(ContentType.TEXT_PLAIN);
        }
        response.setContent(content);

        if(putting && decks.size() != 4) {
            content = "failed";
            response.setStatusCode(StatusCode.BAD_REQUEST);
            response.setContentType(ContentType.TEXT_PLAIN);
            response.setContent(content);
        }

        return response;
    }
}
