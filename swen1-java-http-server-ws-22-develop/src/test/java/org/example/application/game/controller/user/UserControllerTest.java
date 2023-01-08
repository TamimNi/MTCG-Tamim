package org.example.application.game.controller.user;

import org.example.application.game.model.user.User;
import org.example.application.game.repository.user.UserMemoryRepository;
import org.example.application.game.repository.user.UserRepository;
import org.example.server.dto.Response;
import org.example.server.http.StatusCode;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    @Test
    void testSetStatusCode() {
        Response response = new Response();

        response.setStatusCode(StatusCode.OK);

        assertEquals(StatusCode.OK.code, response.getStatus());
        assertEquals(StatusCode.OK.message, response.getMessage());
    }

    @Test
    void testGetUser() throws SQLException {
        User user = new User();
        UserRepository userRepository = new UserMemoryRepository();

        user = userRepository.getUser("Basic kienboec-mtcgToken", "kienboec");
        assertEquals(user.getUsername(), "kienboec");
        assertTrue(user.getBio() != null);
    }

    @Test
    void testGetUserNoValidUsername() throws SQLException {
        User user = new User();
        UserRepository userRepository = new UserMemoryRepository();

        user = userRepository.getUser("Basic kienboec-mtcgToken", "qwe");

        assertNotEquals(user.getUsername(), "kienboec");
        assertTrue(user.getBio() == null);
    }

    @Test
    void testGetUserNoValidToken() throws SQLException {
        User user = new User();
        UserRepository userRepository = new UserMemoryRepository();

        user = userRepository.getUser("Basic wrong-mtcgToken", "kienboec");

        assertNotEquals(user.getUsername(), "kienboec");
        assertTrue(user.getBio() == null);
    }

    @Test
    void testGetUserNoValidCredentialCombination() throws SQLException {
        User user = new User();
        UserRepository userRepository = new UserMemoryRepository();

        user = userRepository.getUser("Basic kienboec-mtcgToken", "admin");

        assertNotEquals(user.getUsername(), "kienboec");
        assertNotEquals(user.getUsername(), "admin");
        assertTrue(user.getBio() == null);
    }
    @Test
    void editUser() throws SQLException {
        User user = new User();
        UserRepository userRepository = new UserMemoryRepository();
        user.setName("Kibo");
        user.setBio("tester..");
        user.setImage(":P");

        user = userRepository.editUser("Basic kienboec-mtcgToken", "kienboec", user);

        assertNotEquals(user.getUsername(), null);
        assertNotEquals(user.getUsername(), null);
        assertNotEquals(user.getUsername(), null);
    }

    @Test
    void editUserUsernameStaysSame() throws SQLException {
        User user = new User();
        UserRepository userRepository = new UserMemoryRepository();
        user.setName("Kibo");
        user.setBio("tester..");
        user.setImage(":P");
        user.setUsername("KiBo");
        User before = userRepository.getUser("Basic kienboec-mtcgToken", "kienboec");

        user = userRepository.editUser("Basic kienboec-mtcgToken", "kienboec", user);

        assertEquals(user.getUsername(),before.getUsername());
    }

    @Test
    void editNotAbleForOthers() throws SQLException {
        User user = new User();
        UserRepository userRepository = new UserMemoryRepository();
        user.setName("Kibo");
        user.setBio("tester..");
        user.setImage(":P");
        user.setUsername("KiBo");

        user = userRepository.editUser("Basic kienboec-mtcgToken", "altenhof", user);
        assertEquals(user.getUsername(),null);
    }
}