package org.example.application.game.repository.cards;
import org.example.application.game.databse.repository.databaseMemoryRepository;
import org.example.application.game.model.cards.Cards;
import org.example.application.game.model.transaction.Transactions;
import org.example.server.dto.Request;
import org.example.server.dto.Request;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
public class CardsMemoryRepository implements CardsRepository{
    databaseMemoryRepository db = new databaseMemoryRepository();
    @Override
    public List<Cards> getAquiredCards(Request request) throws SQLException{
        //System.out.println("Token "+request.getAuthorization());
        List<Cards> cardsList = new ArrayList<>();

        try {
            db.connect();
            List<Map<String, Object>> cardsFromDB = db.aquiredCardsFromDB(request.getAuthorization());
            db.close();
            for(Map<String, Object> crd : cardsFromDB){
                Cards cards = new Cards();
                cards.setId((String) crd.get("id"));
                cards.setName((String) crd.get("name"));
                cards.setDamage((Double) crd.get("damage"));
                //System.out.println("das hier: "+cards);
                cardsList.add(cards);
            }

        }catch (Exception e){ }
        return cardsList;

    }
}
