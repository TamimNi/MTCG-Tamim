package org.example.application.game.controller.session;

import org.example.application.game.model.session.Session;
import org.example.application.game.repository.session.SessionMemoryRepository;
import org.example.application.game.repository.session.SessionRepository;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class SessionControllerTest {
    @Test
    void logIn() throws SQLException {
        Session session = new Session();
        SessionRepository sessionRepository = new SessionMemoryRepository();
        session.setUsername("kienboec");
        session.setPassword("daniel");

        session = sessionRepository.verify(session);

        assertTrue(session.getToken() != null);
    }
    @Test
    void logInWrongPasswort() throws SQLException {
        Session session = new Session();
        SessionRepository sessionRepository = new SessionMemoryRepository();
        session.setUsername("kienboec");
        session.setPassword("da");

        session = sessionRepository.verify(session);

        assertTrue(session == null);
    }
    @Test
    void logInWrongUsername() throws SQLException {
        Session session = new Session();
        SessionRepository sessionRepository = new SessionMemoryRepository();
        session.setUsername("kin");
        session.setPassword("daniel");

        session = sessionRepository.verify(session);

        assertTrue(session == null);
    }
    @Test
    void logInPasswortNotSeen() throws SQLException {
        Session session = new Session();
        SessionRepository sessionRepository = new SessionMemoryRepository();
        session.setUsername("kienboec");
        session.setPassword("daniel");
        String pass = session.getPassword();

        session = sessionRepository.verify(session);

        assertFalse(pass.equals(session.getPassword()));
    }
}