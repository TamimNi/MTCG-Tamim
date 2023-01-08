package org.example.application.game.repository.newcards;

import org.example.application.game.databse.repository.databaseMemoryRepository;
import org.example.application.game.model.newcards.NewCards;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NewCardsMemoryRepository implements NewCardsRepository{
    databaseMemoryRepository db = new databaseMemoryRepository();
    @Override
    public List<NewCards> getCards(String token) throws SQLException {
        List<NewCards> cards = new ArrayList<>();
        db.connect();
        cards = db.getNewCards(token);
        db.close();
        return cards;
    }
}
