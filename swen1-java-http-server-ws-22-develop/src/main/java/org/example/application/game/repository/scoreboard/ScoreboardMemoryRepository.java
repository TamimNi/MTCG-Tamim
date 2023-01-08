package org.example.application.game.repository.scoreboard;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.application.game.databse.repository.databaseMemoryRepository;
import org.example.application.game.model.scoreboard.Scoreboard;
import org.example.server.dto.Request;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ScoreboardMemoryRepository implements ScoreboardRepository{
    databaseMemoryRepository db = new databaseMemoryRepository();
    @Override
    public List<Scoreboard> getScoreboard(Request request) throws SQLException {
        db.connect();
        List<Map<String,Object>> getValues = db.getScoreboardFromDb(request.getAuthorization());
        db.close();
        List<Scoreboard> scoreboard = new ArrayList<>();
        //System.out.println("This are all values "+getValues);
        for(Map<String, Object> scr : getValues){
            ObjectMapper objectMapper = new ObjectMapper();
            Scoreboard score = new Scoreboard();
            score.setUsername((String) scr.get("username"));
            score.setImage((String) scr.get("image"));
            score.setElo((Integer) scr.get("elo"));

            scoreboard.add(score);
        }
        Collections.sort(scoreboard, (o1, o2) -> Integer.compare(o2.getElo(), o1.getElo()));
        return scoreboard;

    }
}
