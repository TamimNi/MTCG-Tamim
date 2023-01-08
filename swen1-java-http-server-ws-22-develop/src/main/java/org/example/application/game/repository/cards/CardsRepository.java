package org.example.application.game.repository.cards;

import org.example.application.game.model.cards.Cards;
import org.example.server.dto.Request;

import java.sql.SQLException;
import java.util.List;

public interface CardsRepository {

    List<Cards> getAquiredCards(Request request) throws SQLException;
}
