package org.example.application.game.controller.battle;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.application.game.model.battle.Battle;
import org.example.application.game.repository.battle.*;
import org.example.server.dto.Request;
import org.example.server.dto.Response;
import org.example.server.http.ContentType;
import org.example.server.http.Method;
import org.example.server.http.StatusCode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;

public class BattleController {
    private final BattleRepository battleRepository;

    public BattleController(BattleRepository battleRepository) {
        this.battleRepository = battleRepository;
    }

    public Response handle(Request request) throws SQLException {
        if (request.getMethod().equals(Method.POST.method)) {
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


        String json = request.getContent();
        Battle battle = null;



        battle = battleRepository.fight(request.getAuthorization());

        content = battle.getResultFight();
        if(!battle.isContentSend1()){
            battle.setContentSend1(true);
        }
        else{
            //System.out.println(battle.getPlayerB());
            battle.resetStatic();
            //System.out.println(battle.getPlayerB());
        }
        /*try {
            content = objectMapper.writeValueAsString(battle);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }*/
        Response response = new Response();
        response.setStatusCode(StatusCode.OK);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setContent(content);
        return response;
    }
}
