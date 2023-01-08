package org.example.application.game.repository.deck;

import org.example.application.game.databse.repository.databaseMemoryRepository;
import org.example.application.game.model.cards.Cards;
import org.example.application.game.model.deck.Deck;
import org.example.application.game.model.transaction.Transactions;
import org.example.server.dto.Request;
import org.example.server.dto.Response;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DeckMemoryRepository implements DeckRepository{
    databaseMemoryRepository db = new databaseMemoryRepository();
    @Override
    public List<Deck> chooseDeckCards(List<Deck> deck, Request request) throws SQLException {
        db.connect();
        //db.deckCardsToDB(deck, request.getAuthorization());
        List<Deck> deckList = new ArrayList<>();
        if(deck.size()==4) {
            List<Map<String, Object>> getDeck = db.deckCardsToDB(deck, request.getAuthorization());
            db.close();
            for (Map<String, Object> dck : getDeck) {
                Deck deckEntry = new Deck();
                deckEntry.setId((String) dck.get("id"));
                deckEntry.setName((String) dck.get("name"));
                deckEntry.setDamage((Double) dck.get("damage"));
                deckList.add(deckEntry);
            }
        }
        return deckList;
    }
    @Override
    public List<Deck> getDeckCards(Request request) throws SQLException{
        db.connect();
        List<Map<String, Object>> getDeck = db.deckCardsFromDB(request.getAuthorization());
        db.close();
        List<Deck> deckList = new ArrayList<>();
        for(Map<String, Object> dck : getDeck){
            Deck deckEntry = new Deck();
            deckEntry.setId((String) dck.get("id"));
            deckEntry.setName((String) dck.get("name"));
            deckEntry.setDamage((Double) dck.get("damage"));
            deckList.add(deckEntry);
        }
        return deckList;

    }

}
