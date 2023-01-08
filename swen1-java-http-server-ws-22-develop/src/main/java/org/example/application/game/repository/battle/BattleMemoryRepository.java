package org.example.application.game.repository.battle;

import org.example.application.game.databse.repository.databaseMemoryRepository;
import org.example.application.game.model.battle.Battle;

import java.sql.SQLException;
import java.util.*;

public class BattleMemoryRepository implements BattleRepository {
    private String log;
    databaseMemoryRepository db = new databaseMemoryRepository();
    @Override
    public Battle fight(String token) throws SQLException {
        boolean host = false;
        String log = "";
        Battle battle = new Battle();
        db.connect();
        String username = db.getSessionUserFromDb(token);
        List<Map<String, Object>> deckCards = db.deckCardsFromDB(token);
        db.close();
        //System.out.println("this is the username: " + username);
        host = battle.setPlayer(username, deckCards);
        //System.out.println(deckCards);
        //System.out.println(battle.getPlayerA() + " " +
        //battle.getPlayerB() + "\n" + battle.getDeckCards1() + "\n" + battle.getDeckCards2());

        boolean won = false;
        int roundsLimit = 0;
        if(host) {
            List<Map<String,Object>> deckPlayer1 = battle.getDeckCards1();
            List<Map<String,Object>> deckPlayer2 = battle.getDeckCards2();
            // Shuffle the list
            Collections.shuffle(deckPlayer1);
            Collections.shuffle(deckPlayer2);
            // Generate a random index
            Random random = new Random();
            int index1, index2;
            int win1 = 0, win2 = 0, draw = 0;
            Map<String, Object> cardP1, cardP2;
            int winStreak1 = 0, winStreak2 = 0;
            log += "host found\n\n" + battle.getPlayerA() + " vs " + battle.getPlayerB() + "\n";
            do {
                List<Double> dmg = new ArrayList<>();
                roundsLimit++;
                index1 = random.nextInt(deckPlayer1.size());
                index2 = random.nextInt(deckPlayer2.size());
                // Get the element at the random index
                Map<String, Object> randomCard1 = deckPlayer1.get(index1);
                Map<String, Object> randomCard2 = deckPlayer2.get(index2);
               // System.out.println(roundsLimit+"Random card: " + randomCard1);
              //  System.out.println("Random card: " + randomCard2);
                cardP1 = extractCardInfo(deckPlayer1.get(index1));
                cardP2 = extractCardInfo(deckPlayer2.get(index2));
               // System.out.println(cardP1 + "\n" +cardP2);
                dmg = calculateDMG(cardP1, cardP2);
             //   System.out.println("DAMAGE\n"+dmg);

                if(dmg.get(0) > dmg.get(1)){
                    log += deckPlayer1.get(index1).toString() + "\n" + deckPlayer2.get(index2).toString() + "\n" +
                     dmg.get(0) + " > " +dmg.get(1) + "\nPlayer1.\n";
                    win1++;
                    //System.out.println("w1");
                    deckPlayer1.add(deckPlayer2.get(index2));
                    deckPlayer2.remove(index2);
                    winStreak1++;
                    winStreak2--;
                }
                else if(dmg.get(0) < dmg.get(1)) {
                    log += deckPlayer1.get(index1).toString() + "\n" + deckPlayer2.get(index2).toString() + "\n" +
                            dmg.get(0) + " < " +dmg.get(1) + "\nPlayer2.\n";
                    win2++;
                  //  System.out.println("w2");
                    deckPlayer2.add(deckPlayer1.get(index1));
                    deckPlayer1.remove(index1);
                    winStreak1--;
                    winStreak2++;
                }
                else {
                    log += deckPlayer1.get(index1).toString() + "\n" + deckPlayer2.get(index2).toString() + "\n" +
                            dmg.get(0) + " = " +dmg.get(1) + "\nDRAW.\n";
                  //  System.out.println("draw");
                    draw++;
                }
                if(winStreak1 >= 5 || winStreak2 >= 5){
                    won = true;
                }
            } while (!won && roundsLimit != 100);
                battle.setGameOver(true);

            log += "\nGAME OVER\n\nrounds: " + roundsLimit + "\n" + "CardsOpponent: " +  winStreak1 + " : " + winStreak2 + "\nWinner: ";
            if(winStreak1 > 0){
                log += "Player1";
                db.connect();
                db.fightToDB(battle.getPlayerA(),battle.getPlayerB());
                db.close();
            }
            else if(winStreak2 > 0){
                log += "Player2";
                db.connect();
                db.fightToDB(battle.getPlayerB(),battle.getPlayerA());
                db.close();
            }
            else{
                log += "DRAW";
            }
            battle.setResultFight(log);
            //System.out.println("w1 " + win1 + "\nw2 " + win2 + "\nstreak: ");
            //System.out.println(winStreak1 + " : " + winStreak2);
            //System.out.println("draws " + draw);
            //System.out.println("rounds" + roundsLimit);
        }else{
            try {
                do{
                // Sleep for 1 second
                Thread.sleep(1000);
                }while(!battle.isGameOver());
                //System.out.println("Opponent.. Game Over" + battle.getResultFight());
            } catch (InterruptedException e) {
                // Handle the exception
                //System.out.println("Ex : "+e);
            }
        }
        return battle;
    }
    public Map<String, Object> extractCardInfo(Map<String, Object> card){
        Map<String, Object> cardInfo = new HashMap<>();
        boolean isSpell = false;
        String element_Type = "";
        String monsterType = "";
        String cardName = (String) card.get("name");
        if(cardName.toLowerCase().contains("spell"))
            isSpell = true;
        if(cardName.toLowerCase().contains("fire"))
            element_Type = "fire";
        else if(cardName.toLowerCase().contains("water"))
            element_Type = "water";
        else if(cardName.toLowerCase().contains("regular"))
            element_Type = "regular";
        else
            element_Type = "regular";

        if(!isSpell)
        {
            if(cardName.toLowerCase().contains("goblin"))
                monsterType = "goblin";
            if(cardName.toLowerCase().contains("dragon"))
                monsterType = "dragon";
            if(cardName.toLowerCase().contains("ork"))
                monsterType = "ork";
            if(cardName.toLowerCase().contains("knight"))
                monsterType = "knight";
            if(cardName.toLowerCase().contains("elf"))
                monsterType = "elf";
            if(cardName.toLowerCase().contains("wizard"))
                monsterType = "wizard";
            if(cardName.toLowerCase().contains("kraken"))
                monsterType = "kraken";
        }
        cardInfo.put("isSpell", isSpell);
        cardInfo.put("element", element_Type);
        cardInfo.put("monster", monsterType);
        cardInfo.put("damage", card.get("damage"));
        return cardInfo;
    }

    public List<Double> calculateDMG(Map<String, Object> cardP1, Map<String, Object> cardP2){
     List<Double> dmg = new ArrayList<>();
        Double dmgP1 = (Double) cardP1.get("damage"), dmgP2 = (Double) cardP2.get("damage");
    String elementP1 = (String) cardP1.get("element"), elementP2 = (String) cardP2.get("element");
         String nameP1 = (String) cardP1.get("monster"), nameP2 = (String) cardP2.get("monster");
        if((boolean) cardP1.get("isSpell") && (boolean) cardP2.get("isSpell")){ //SpellFight
            if(elementP1.equals("water") && elementP2.equals("fire")){ dmgP1 = dmgP1*2; dmgP2 = dmgP2/2;}
            else if(elementP1.equals("fire") && elementP2.equals("regular")){ dmgP1 = dmgP1*2; dmgP2 = dmgP2/2;}
            else if(elementP1.equals("regular") && elementP2.equals("water")){ dmgP1 = dmgP1*2; dmgP2 = dmgP2/2;}

            else if(elementP1.equals("fire") && elementP2.equals("water")){ dmgP1 = dmgP1/2; dmgP2 = dmgP2*2;}
            else if(elementP1.equals("regular") && elementP2.equals("fire")){ dmgP1 = dmgP1/2; dmgP2 = dmgP2*2;}
            else if(elementP1.equals("water") && elementP2.equals("regular")){ dmgP1 = dmgP1/2; dmgP2 = dmgP2*2;}
            //else dmg stays the same
        }else if(!((boolean) cardP1.get("isSpell")) && !((boolean) cardP2.get("isSpell"))){//MonsterFight
            if(nameP1.equals("goblin") && nameP2.equals("dragon")){ dmgP1 = 0.0;}
            else if(nameP1.equals("wizzard") && nameP2.equals("ork")){ dmgP2 = 0.0;}
            else if(nameP1.equals("elf") && nameP2.equals("dragon")){ dmgP2 = 0.0;}

            else if(nameP1.equals("dragon") && nameP2.equals("goblin")){ dmgP2 = 0.0;}
            else if(nameP1.equals("ork") && nameP2.equals("wizzard")){ dmgP1 = 0.0;}
            else if(nameP1.equals("dragon") && nameP2.equals("elf")){ dmgP1 = 0.0;}
            //else dmg stays the same

        }else{//Mix
            if(elementP1.equals("water") && nameP2.equals("knight")){ dmgP2 = 0.0;}
            else if(nameP1.equals("knight") && elementP2.equals("water")){ dmgP1 = 0.0;}

            /*else if(nameP1.equals("kraken")){ dmgP2 = 0.0;}
            else if(nameP2.equals("kraken")){ dmgP1 = 0.0;}
*/
            else if(elementP1.equals("water") && elementP2.equals("fire")){ dmgP1 = dmgP1*2; dmgP2 = dmgP2/2;}
            else if(elementP1.equals("fire") && elementP2.equals("regular")){ dmgP1 = dmgP1*2; dmgP2 = dmgP2/2;}
            else if(elementP1.equals("regular") && elementP2.equals("water")){ dmgP1 = dmgP1*2; dmgP2 = dmgP2/2;}

            else if(elementP1.equals("fire") && elementP2.equals("water")){ dmgP1 = dmgP1/2; dmgP2 = dmgP2*2;}
            else if(elementP1.equals("regular") && elementP2.equals("fire")){ dmgP1 = dmgP1/2; dmgP2 = dmgP2*2;}
            else if(elementP1.equals("water") && elementP2.equals("regular")){ dmgP1 = dmgP1/2; dmgP2 = dmgP2*2;}

            //else dmg stays the same
        }
        dmg.add(dmgP1);
        dmg.add(dmgP2);
        return (dmg);
    }
}
