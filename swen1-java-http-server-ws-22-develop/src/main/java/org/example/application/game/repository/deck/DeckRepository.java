package org.example.application.game.repository.deck;

import org.example.application.game.model.deck.Deck;
import org.example.server.dto.Request;
import org.example.server.dto.Response;

import java.sql.SQLException;
import java.util.List;

public interface DeckRepository {
    List<Deck> chooseDeckCards(List<Deck> deck, Request request) throws SQLException;
    List<Deck> getDeckCards(Request request) throws SQLException;
}
