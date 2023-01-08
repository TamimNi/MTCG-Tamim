package org.example.application.game.repository.user;
import org.example.application.game.databse.repository.databaseMemoryRepository;
import org.example.application.game.model.deck.Deck;
import org.example.application.game.model.user.User;
//import org.example.application.game.databse.*;
import org.example.PasswordAuthentication;
import org.example.server.dto.Request;

import java.sql.SQLException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserMemoryRepository implements UserRepository{

    databaseMemoryRepository db = new databaseMemoryRepository();

    @Override
    public User save(User user) throws SQLException {
        //password hash + user to db//put in db and return user
        //user.setPassword(user.getPassword());
        boolean success = false;
        db.connect();
        success = db.userToDb(user.getUsername(), user.getPassword());
        db.close();
        user.setPassword("******");
        user.setCoins(20);
        user.setImage("^_^");
        user.setBio("new player");
        user.setName("newbie");
        if(success) return user;
        else return null;
    }

    @Override
    public User getUser(String token, String username) throws SQLException{
        db.connect();
        List<Map<String, Object>> getUser = db.userFromDB(token, username);
        db.close();

        User userEntry = new User();
        for (Map<String, Object> usr : getUser) { //only one user expected
            userEntry.setUsername((String) usr.get("username"));
            userEntry.setPassword("******");
            userEntry.setBio((String) usr.get("bio"));
            userEntry.setImage((String) usr.get("image"));
            userEntry.setCoins((Integer) usr.get("coins"));
            userEntry.setName((String) usr.get("name"));
        }
        return userEntry;
    }

    @Override
    public User editUser(String token, String username, User user) throws SQLException{
        db.connect();
        List<Map<String, Object>> getUser = db.editUserInDB(token, username, user.getName(), user.getBio(), user.getImage());
        db.close();

        User userEntry = new User();
        for (Map<String, Object> usr : getUser) { //only one user expected
            userEntry.setUsername((String) usr.get("username"));
            userEntry.setPassword("******");
            userEntry.setBio((String) usr.get("bio"));
            userEntry.setImage((String) usr.get("image"));
            userEntry.setCoins((Integer) usr.get("coins"));
            userEntry.setName((String) usr.get("name"));
        }
        return userEntry;
    }
}
