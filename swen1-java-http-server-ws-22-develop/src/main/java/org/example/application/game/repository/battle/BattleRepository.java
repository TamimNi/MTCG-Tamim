package org.example.application.game.repository.battle;

import org.example.application.game.model.battle.Battle;
import org.example.application.game.model.user.User;

import java.sql.SQLException;

public interface BattleRepository {
    Battle fight(String token) throws SQLException;
}
