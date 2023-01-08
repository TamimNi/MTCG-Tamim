package org.example.application.game.repository.tradings;

import org.example.application.game.model.tradings.Trading;
import org.example.server.dto.Request;

import java.sql.SQLException;
import java.util.List;

public interface TradingRepository {
    List<Trading> getTrades(Request request) throws SQLException;
    Trading offerTrade(Request request, Trading trading) throws SQLException;
    Trading aquireTrade(Request request, String tradeID, String cardToTrade) throws SQLException;
    String cancelTrade(Request request, String tradeID) throws SQLException;
}
