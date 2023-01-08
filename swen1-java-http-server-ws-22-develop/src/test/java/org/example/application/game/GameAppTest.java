package org.example.application.game;

import org.example.server.dto.Request;
import org.example.server.dto.Response;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class GameAppTest {
    @Test
    void wrongPath() throws SQLException {
        GameApp gameApp = new GameApp();
        Request request = new Request();
        Response response = new Response();
        request.setPath("/usaa");

        response = gameApp.handle(request);
        System.out.println(response.getStatus());
        assertEquals(response.getStatus(),404);
    }

}