package org.example.application.game.repository.tradings;

import org.example.application.game.databse.repository.databaseMemoryRepository;
import org.example.application.game.model.tradings.Trading;
import org.example.server.dto.Request;

import java.sql.SQLException;
import java.util.List;

public class TradingMemoryRepository implements TradingRepository{
    databaseMemoryRepository db = new databaseMemoryRepository();

    @Override
    public List<Trading> getTrades(Request request) throws SQLException{
        db.connect();
        List<Trading> trades = db.tradesFromDB(request.getAuthorization());
        db.close();
        return trades;
    }

    @Override
    public Trading offerTrade(Request request, Trading trading) throws SQLException{
        db.connect();
        trading = db.tradeOfferToDB(request.getAuthorization(), trading);
        db.close();
        return trading;
    }

    @Override
    public Trading aquireTrade(Request request, String tradeID, String cardToTrade) throws SQLException{
        db.connect();
        Trading trade = db.aquireTradeInDB(request.getAuthorization(), tradeID, cardToTrade);
        db.close();
        return trade;
    }

    @Override
    public String cancelTrade(Request request, String tradeID) throws SQLException {
        db.connect();
        tradeID = db.cancelTradeInDB(request.getAuthorization(), tradeID);
        db.close();
        return tradeID;
    }
}
