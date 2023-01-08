package org.example.application.game.repository.session;

import org.example.application.game.databse.repository.databaseMemoryRepository;
import org.example.application.game.model.session.Session;
import java.sql.SQLException;

public class SessionMemoryRepository implements SessionRepository {
    databaseMemoryRepository db = new databaseMemoryRepository();

    @Override
    public Session verify(Session session) throws SQLException {
        db.connect();
        //password hash + user to db//put in db and return user
        String storedPW = db.passwordFromDb(session.getUsername());
        if (session.getPassword().equals(storedPW)) {
            session.setPassword("*****");
            session.setToken(session.getUsername()  + "-mtcgToken");
            db.tokenToDB(session.getUsername(), session.getToken());
            db.close();
            return session;
        } else return null;
    }
}
