package org.example.application.game.controller.session;

import org.example.application.game.model.session.Session;
import org.example.application.game.repository.session.SessionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.server.dto.Request;
import org.example.server.dto.Response;
import org.example.server.http.ContentType;
import org.example.server.http.Method;
import org.example.server.http.StatusCode;
import java.sql.SQLException;

public class SessionController {
    private final SessionRepository sessionRepository;

    public SessionController(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
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

        String json = request.getContent();
        Session session;
        try {
            session = objectMapper.readValue(json, Session.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        //User user = this.userRepository.register(new User(request.getContent(),request.getPath()));
        session = sessionRepository.verify(session);

        Response response = new Response();
        response.setStatusCode(StatusCode.OK);
        response.setContentType(ContentType.APPLICATION_JSON);
        String content = null;
        try {
            content = objectMapper.writeValueAsString(session);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        response.setContent(content);
        if(session == null) {
            content = "failed";
            response.setStatusCode(StatusCode.BAD_REQUEST);
            response.setContentType(ContentType.TEXT_PLAIN);
            response.setContent(content);
        }
        return response;
    }
}
