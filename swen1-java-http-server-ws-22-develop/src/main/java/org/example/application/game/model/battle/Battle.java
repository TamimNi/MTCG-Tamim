package org.example.application.game.model.battle;

import java.util.List;
import java.util.Map;

public class Battle {
    private static String playerA = null;
    private static String playerB = null;
    private static boolean gameOver = false;
    private static String resultFight = null;
    private static boolean contentSend1 = false;
    private static List<Map<String, Object>> deckCards1 = null;
    private static List<Map<String, Object>> deckCards2 = null;
    public String getPlayerA() {
        return playerA;
    }

    public String getPlayerB() {
        return playerB;
    }
    public List<Map<String, Object>> getDeckCards1() {
        return deckCards1;
    }

    public List<Map<String, Object>> getDeckCards2() {
        return deckCards2;
    }
    public String getResultFight() {
        return resultFight;
    }

    public void setPlayerA(String playerA) {
        this.playerA = playerA;
    }

    public void setPlayerB(String playerB) {
        this.playerB = playerB;
    }

    public boolean setPlayer(String player, List<Map<String, Object>> deckCards) {
        boolean host = false;
        if (playerA == null) {
            playerA = player;
            deckCards1 = deckCards;
            return host;
        } else if (playerB == null) {
            playerB = player;
            deckCards2 = deckCards;
            host = true;
            return host;
        }
        return false;
    }


    public void setResultFight(String resultFight) {
        Battle.resultFight = resultFight;
    }


    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        Battle.gameOver = gameOver;
    }

    public boolean isContentSend1() {
        return contentSend1;
    }

    public void setContentSend1(boolean contentSend1) {
        Battle.contentSend1 = contentSend1;
    }
    public void resetStatic(){
        playerA = null;
        playerB = null;
        gameOver = false;
        resultFight = null;
        contentSend1 = false;
        deckCards1 = null;
        deckCards2 = null;

    }
}
