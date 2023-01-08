package org.example.application.game.repository.transaction;
import org.example.application.game.model.transaction.Transactions;
import org.example.server.dto.Request;

import java.sql.SQLException;

import java.util.List;
public interface TransactionRepository {
    List<Transactions> aquirePackage(Request request) throws SQLException;
}
