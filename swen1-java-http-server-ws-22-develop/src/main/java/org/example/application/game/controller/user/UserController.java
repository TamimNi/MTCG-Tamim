package org.example.application.game.controller.user;

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
import java.util.List;

public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Response handle(Request request) throws SQLException {
        if (request.getMethod().equals(Method.POST.method) || request.getMethod().equals(Method.PUT.method) || request.getMethod().equals(Method.GET.method)) {
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
        boolean editUser = false, getUser = false;
        String content = null;

/*String str = "/users/kienboec";

// Split the string into an array of substrings based on the '/' character
String[] parts = str.split("/");

if (parts.length > 2) {
    // There is something after the '/users' part of the string
    String afterUsers = parts[2];
    System.out.println("There is something after '/users': " + afterUsers);
} else {
    // There is nothing after the '/users' part of the string
    System.out.println("There is nothing after '/users'");
}*/
        String[] pathUsername = request.getPath().split("/");
        if (pathUsername.length > 2) {
            editUser = true;
        }
        if(request.getContentLength() == 0){
            getUser = true;
        }

        String json = request.getContent();
        User user = null;
        if(!getUser) {
            try {
                user = objectMapper.readValue(json, User.class);
            } catch (JsonProcessingException e) {
                System.out.println(" :( " + e);
                throw new RuntimeException(e);
            }
        }
        if(!editUser) {//POST create user
            user = userRepository.save(user);
        }
        else{
            if(request.getMethod().equals(Method.PUT.method) && !getUser) {//PUT edit user
                user = userRepository.editUser(request.getAuthorization(), pathUsername[2], user);
            }else{//GET user
                user = userRepository.getUser(request.getAuthorization(), pathUsername[2]);
            }
        }

        try {
            content = objectMapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Response response = new Response();
        response.setStatusCode(StatusCode.OK);
        response.setContentType(ContentType.APPLICATION_JSON);
        response.setContent(content);
        if(!editUser){ response.setStatusCode(StatusCode.CREATED);}
        if(user == null || user.getUsername() == null) {
            content = "failed";
            response.setStatusCode(StatusCode.BAD_REQUEST);
            response.setContentType(ContentType.TEXT_PLAIN);
            response.setContent(content);
        }
        return response;
    }
}
