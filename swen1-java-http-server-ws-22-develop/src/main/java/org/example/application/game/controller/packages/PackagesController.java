package org.example.application.game.controller.packages;

import org.example.application.game.model.packages.Packages;
import com.fasterxml.jackson.core.type.TypeReference;
import org.example.application.game.repository.packages.PackagesRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.server.dto.Request;
import org.example.server.dto.Response;
import org.example.server.http.ContentType;
import org.example.server.http.Method;
import org.example.server.http.StatusCode;

import java.sql.SQLException;
import java.util.List;

public class PackagesController {
    private final PackagesRepository packagesRepository;

    public PackagesController(PackagesRepository packagesRepository) {
        this.packagesRepository = packagesRepository;
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
        List<Packages> packages;
        //System.out.println("..");
        try {
            packages = objectMapper.readValue(json, new TypeReference<List<Packages>>(){});

            //System.out.println(packages);
            //System.out.println("hello");
        } catch (JsonProcessingException e) {
            //System.out.println("DAMN" + e);

            throw new RuntimeException(e);
        }
        //System.out.println("hi");
        //User user = this.userRepository.register(new User(request.getContent(),request.getPath()));
        packages = packagesRepository.createPackage(packages, request);
        /*for (Package pkg : packages) {
            System.out.println("Id: " + pkg.getId());
            System.out.println("Name: " + pkg.getName());
            System.out.println("Damage: " + pkg.getDamage());
        }*/
        /*System.out.println(packages);
        for (Packages pkg : packages) {
            System.out.println("Id: " + pkg.getId());
            System.out.println("Name: " + pkg.getName());
            System.out.println("Damage: " + pkg.getDamage());
        }*/
        Response response = new Response();
        response.setStatusCode(StatusCode.CREATED);
        response.setContentType(ContentType.APPLICATION_JSON);
        String content = null;
        try {
            content = objectMapper.writeValueAsString(packages);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        response.setContent(content);

        if(packages == null) {
            content = "failed";
            response.setStatusCode(StatusCode.BAD_REQUEST);
            response.setContentType(ContentType.TEXT_PLAIN);
            response.setContent(content);
        }
        return response;
    }
}
