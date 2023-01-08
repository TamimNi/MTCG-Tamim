package org.example.application.game.repository.stats;
import org.example.application.game.databse.repository.databaseMemoryRepository;
import org.example.application.game.model.stats.*;
import org.example.server.dto.Request;

import java.sql.SQLException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StatsMemoryRepository implements StatsRepository{
    databaseMemoryRepository db = new databaseMemoryRepository();
    @Override
    public Stats getStatOfUser(Request request) throws SQLException {
        db.connect();
        Stats stats = new Stats();
        Map<String,Object> getValues = db.getStatsOfUser(request.getAuthorization());
        db.close();
        stats.setUsername((String) getValues.get("username"));
        stats.setImage((String) getValues.get("image")) ;
        stats.setElo((Integer) getValues.get("elo"));
        return stats;

    }
}
