package org.example.application.game.repository.newcards;

import org.example.application.game.model.newcards.NewCards;

import java.sql.SQLException;
import java.util.List;

public interface NewCardsRepository {
    public List<NewCards> getCards(String token) throws SQLException;
}
