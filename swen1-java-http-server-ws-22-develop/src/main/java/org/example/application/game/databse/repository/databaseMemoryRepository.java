package org.example.application.game.databse.repository;

import org.example.application.game.model.deck.Deck;
import org.example.application.game.model.newcards.NewCards;
import org.example.application.game.model.packages.Packages;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.example.application.game.model.deck.Deck;
        import org.example.application.game.model.packages.Packages;
import org.example.application.game.model.tradings.Trading;
import org.example.application.game.repository.newcards.NewCardsRepository;

import java.sql.*;
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

public class databaseMemoryRepository {
    private final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private final String DB_USER = "postgres";
    private final String DB_PW = "postgres";
    public Connection conn;

    public void connect() throws SQLException {
        conn = DriverManager.getConnection(
                DB_URL, DB_USER, DB_PW
        );
    }
    public void close() throws SQLException {
        conn.close();
    }
    public boolean userToDb(String username, String password) throws SQLException {
        String preparedSql = "INSERT INTO users(username, password) VALUES(?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(preparedSql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.execute();
        }
        catch(Exception e){
            return false;
        }
        return true;
    }

    public String passwordFromDb(String username) throws SQLException {
        //String preparedSql = "SELECT password FROM users WHERE `username` = VALUES(?)";
        String preparedSql = "SELECT * FROM users WHERE username = ?";
        String result = null;
        try (PreparedStatement ps = conn.prepareStatement(preparedSql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = (rs.getString("password"));
                }
            }
        }
        return result;
    }

    public void tokenToDB(String username, String token) throws SQLException {

        String preparedSql3 = "DELETE FROM session WHERE username = ?";
        try (PreparedStatement ps3 = conn.prepareStatement(preparedSql3)) {
            ps3.setString(1, username);
            ps3.execute();
        }


        String preparedSql2 = "INSERT INTO session(username,token) VALUES(?,?)";
        try (PreparedStatement ps2 = conn.prepareStatement(preparedSql2)) {
            ps2.setString(1, username);
            ps2.setString(2, token);
            ps2.execute();
        }
    }

    public boolean packagesToDB(List<Packages> packages, String token) throws SQLException {
        String preparedSql = "INSERT INTO packages(id, name, damage) VALUES(?, ?, ?)";
        String preparedSql2 = "SELECT * FROM session WHERE token = ?";
        boolean isAdmin = false;

        String pureToken = token.substring(6);  // extracts the substring starting at index 6
        try (PreparedStatement ps2 = conn.prepareStatement(preparedSql2)) {
            ps2.setString(1, pureToken);
            try (ResultSet rs = ps2.executeQuery()) {
                //if (rs.getString("username") == "admin" && rs.next()) {
                rs.next();
                if (rs.getString("username").equals("admin"))
                    isAdmin = true;
                //}
                //System.out.println("This is the username rn " + rs.getString("username"));
            }
        }catch (Exception e){return false;}
        int entry = 0;
        if (isAdmin) {
            for (Packages pkg : packages) {
                try (PreparedStatement ps = conn.prepareStatement(preparedSql)) {
                    //entry = true;
                    //ps.setString(1, token);
                    ps.setString(1, pkg.getId());
                    ps.setString(2, pkg.getName());
                    ps.setDouble(3, pkg.getDamage());
                    if(ps.executeUpdate()>0){
                        entry++;
                    }
                    //System.out.println("THAT IS IT "+ps.executeUpdate());
                    //System.out.println("._.");
                }catch (Exception e){ return false;}
            }
        }
        if(entry != 5) return false;
        else
        return true;
    }

    public List<Map<String, Object>> aquirePackageFromDB(String token) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<>();
        String preparedSql = "DELETE FROM packages WHERE id IN (SELECT id FROM packages LIMIT 5) RETURNING *";
        String preparedSql2 = "INSERT INTO aquiredPackages (id, name, damage, username) VALUES (?, ?, ?, ?);";
        //String preparedSql3 = "SELECT username FROM session WHERE token = ?";
        String preparedSql3 = "SELECT s.username, u.coins FROM session AS s JOIN users AS u ON s.username = u.username WHERE s.token = ?";
        String preparedSql4 = "UPDATE users SET coins = coins - 5 WHERE username = ?";
        boolean userLoggedIn = false;
        String username = null;
        int coins = 0;
        String pureToken = token.substring(6);  // extracts the substring starting at index 6
        try (PreparedStatement ps3 = conn.prepareStatement(preparedSql3)) {
            ps3.setString(1, pureToken);
            try (ResultSet rs = ps3.executeQuery()) {
                if (rs.next()) {
                    //eintrag besteht
                    userLoggedIn = true;
                    coins = rs.getInt("coins");
                    //System.out.println("loggedIn");
                    username = rs.getString("username");
                } else System.out.println("fail Logged " + pureToken);
            }
        }
        if (userLoggedIn && coins >= 5) {
            String result = null;
            boolean packageAvailabe = false;
            //System.out.println("HIER");
            try (PreparedStatement ps = conn.prepareStatement(preparedSql)) {
                //System.out.println("_______________");
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> values = new HashMap<>();
                        values.put("id", rs.getString("id"));
                        values.put("name", rs.getString("name"));
                        values.put("damage", rs.getDouble("damage"));
                        list.add(values);


                        try (PreparedStatement ps2 = conn.prepareStatement(preparedSql2)) {
                            ps2.setString(1, rs.getString("id"));
                            ps2.setString(2, rs.getString("name"));
                            ps2.setDouble(3, rs.getDouble("damage"));
                            ps2.setString(4, username);
                            ps2.execute();
                        }
                        packageAvailabe = true;
                    }
                    if (packageAvailabe) {   //return coins username
                        try (PreparedStatement ps4 = conn.prepareStatement(preparedSql4)) {
                            ps4.setString(1, username);
                            ps4.execute();
                        }
                    }
                }
            }
        }
        return list;
    }

    public List<Map<String, Object>> aquiredCardsFromDB(String token) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<>();
        //System.out.println("WE ARE RETRIEVING DATA");
        String preparedSql3 = "SELECT a.id, a.name, a.damage FROM aquiredpackages AS a JOIN session AS s ON s.username = a.username WHERE s.token = ?";
        boolean userLoggedIn = false;
        String id, name;
        double damage;
        String pureToken = token.substring(6);  // extracts the substring starting at index 6
        try (PreparedStatement ps3 = conn.prepareStatement(preparedSql3)) {
            ps3.setString(1, pureToken);
            try (ResultSet rs = ps3.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> values = new HashMap<>();
                    values.put("id", rs.getString("id"));
                    values.put("name", rs.getString("name"));
                    values.put("damage", rs.getDouble("damage"));
                    list.add(values);
                }
            }
        }catch (Exception e){}
        return list;
    }

    public List<Map<String, Object>> deckCardsToDB(List<Deck> deck, String token) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<>();

        //String preparedSql3 = "UPDATE aquiredpackages SET indeck = true WHERE username = (SELECT username FROM session WHERE token = ?) RETURNING *;";
        //String preparedSql3 = "UPDATE aquiredpackages SET indeck = true WHERE username = (SELECT username FROM session WHERE token = ?) AND id IN (?, ?, ?, ?) AND (SELECT COUNT(*) FROM aquiredpackages WHERE indeck = true AND username = (SELECT username FROM session WHERE token = ?)) <= 4 RETURNING *;";
        String preparedSql3 = "UPDATE aquiredpackages\n" +
                "SET indeck = true\n" +
                "WHERE username = (SELECT username FROM session WHERE token = ?)\n" +
                "AND (SELECT COUNT(*) FROM aquiredpackages WHERE indeck = true AND username = (SELECT username FROM session WHERE token = ?)) < 4\n" +
                "AND trading = false\n" +
                "AND id IN (?, ?, ?, ?)\n" +
                "RETURNING *;";
        String id, name;
        double damage;
        String pureToken = token.substring(6);  // extracts the substring starting at index 6
        try (PreparedStatement ps3 = conn.prepareStatement(preparedSql3)) {
            ps3.setString(1, pureToken);
            ps3.setString(2, pureToken);
            ps3.setString(3, deck.get(0).getId());
            ps3.setString(4, deck.get(1).getId());
            ps3.setString(5, deck.get(2).getId());
            ps3.setString(6, deck.get(3).getId());
            try (ResultSet rs = ps3.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> values = new HashMap<>();
                    values.put("id", rs.getString("id"));
                    values.put("name", rs.getString("name"));
                    values.put("damage", rs.getDouble("damage"));
                    list.add(values);
                    //System.out.println(values);
                }

            }
        }
        return list;
    }

    public List<Map<String, Object>> deckCardsFromDB(String token) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<>();
        //System.out.println("WE ARE RETRIEVING DATA");
        String preparedSql3 = "SELECT a.id, a.name, a.damage FROM aquiredpackages AS a JOIN session AS s ON s.username = a.username WHERE s.token = ? AND a.indeck = true";
        boolean userLoggedIn = false;
        String id, name;
        double damage;
        String pureToken = token.substring(6);  // extracts the substring starting at index 6
        try (PreparedStatement ps3 = conn.prepareStatement(preparedSql3)) {
            ps3.setString(1, pureToken);
            try (ResultSet rs = ps3.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> values = new HashMap<>();
                    values.put("id", rs.getString("id"));
                    values.put("name", rs.getString("name"));
                    values.put("damage", rs.getDouble("damage"));
                    list.add(values);
                }
            }
        }
        return list;
    }

    public List<Map<String, Object>> userFromDB(String token, String username) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<>();
        //System.out.println("WE ARE RETRIEVING DATA");
        String preparedSql3 = "SELECT u.username, u.bio, u.image, u.coins, u.name FROM users AS u JOIN session AS s ON s.username = u.username WHERE s.token = ? AND u.username = ? AND s.username = ?";

        String pureToken = token.substring(6);  // extracts the substring starting at index 6

        try (PreparedStatement ps3 = conn.prepareStatement(preparedSql3)) {
            ps3.setString(1, pureToken);
            ps3.setString(2, username);
            ps3.setString(3, username);
            try (ResultSet rs = ps3.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> values = new HashMap<>();
                    values.put("username", rs.getString("username"));
                    values.put("bio", rs.getString("bio"));
                    values.put("image", rs.getString("image"));
                    values.put("coins", rs.getInt("coins"));
                    values.put("name", rs.getString("name"));
                    list.add(values);
                }
            }
        }
        return list;
    }

    public List<Map<String, Object>> editUserInDB(String token, String username, String name, String bio, String image) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<>();

        //String preparedSql3 = "UPDATE aquiredpackages SET indeck = true WHERE username = (SELECT username FROM session WHERE token = ?) RETURNING *;";
        //String preparedSql3 = "UPDATE aquiredpackages SET indeck = true WHERE username = (SELECT username FROM session WHERE token = ?) AND id IN (?, ?, ?, ?) AND (SELECT COUNT(*) FROM aquiredpackages WHERE indeck = true AND username = (SELECT username FROM session WHERE token = ?)) <= 4 RETURNING *;";
        String preparedSql3 = "UPDATE users SET image = ?, bio = ?, name = ? WHERE username = ? AND EXISTS (SELECT 1 FROM session WHERE token = ? AND username = users.username)";
        String pureToken = token.substring(6);  // extracts the substring starting at index 6
        try (PreparedStatement ps3 = conn.prepareStatement(preparedSql3)) {
            ps3.setString(1, image);
            ps3.setString(2, bio);
            ps3.setString(3, name);
            ps3.setString(4, username);
            ps3.setString(5, pureToken);
            ps3.execute();
        }
        return userFromDB(token, username);
    }

    public Map<String, Object> getStatsOfUser(String token) throws SQLException {
        Map<String, Object> values = new HashMap<>();
        String preparedSql = "SELECT username, image, elo FROM users WHERE username = (SELECT username FROM session WHERE token = ?)";
        String pureToken = token.substring(6);
        try (PreparedStatement ps = conn.prepareStatement(preparedSql)) {
            ps.setString(1, pureToken);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    values.put("username", rs.getString("username"));
                    values.put("image", rs.getString("image"));
                    values.put("elo", rs.getInt("elo"));
                }
            }
        }
        return values;
    }
    public List<Map<String, Object>> getScoreboardFromDb(String token) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<>();
        String preparedSql = "SELECT username, image, elo FROM users WHERE EXISTS(SELECT 1 FROM session WHERE token = ?)";
        String pureToken = token.substring(6);
        try (PreparedStatement ps = conn.prepareStatement(preparedSql)) {
            ps.setString(1, pureToken);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> values = new HashMap<>();
                    values.put("username", rs.getString("username"));
                    values.put("image", rs.getString("image"));
                    values.put("elo", rs.getInt("elo"));
                    list.add(values);
                }
            }
        }
        return list;
    }

    public String getSessionUserFromDb(String token) throws SQLException{
        String username = null;
        //System.out.println("WE ARE RETRIEVING DATA");
        String preparedSql = "SELECT username FROM session WHERE token = ?";

        String pureToken = token.substring(6);  // extracts the substring starting at index 6

        try (PreparedStatement ps = conn.prepareStatement(preparedSql)) {
            ps.setString(1, pureToken);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                   username = rs.getString("username");
                }
            }
        }
        return username;
    }

    public void fightToDB(String usernameWinner, String usernameLoser) throws SQLException{
        String preparedSql4 = "UPDATE users SET elo = elo + 3 WHERE username = ?";
        try (PreparedStatement ps4 = conn.prepareStatement(preparedSql4)) {
            ps4.setString(1, usernameWinner);
            ps4.execute();
        }
        String preparedSql3 = "UPDATE users SET elo = elo - 5 WHERE username = ?";
        try (PreparedStatement ps4 = conn.prepareStatement(preparedSql3)) {
            ps4.setString(1, usernameLoser);
            ps4.execute();
        }
    }
    public List<Trading> tradesFromDB(String token){
        String preparedSql = "SELECT * FROM trades WHERE username != (SELECT username FROM session WHERE token = ?) AND (SELECT COUNT(*) FROM session WHERE token = ? ) > 0";
        String pureToken = token.substring(6);  // extracts the substring starting at index 6
        List<Trading> trades = new ArrayList<>();
        try(PreparedStatement ps = conn.prepareStatement(preparedSql)){
            ps.setString(1, pureToken);
            ps.setString(2, pureToken);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Trading trade = new Trading();
                    trade.setTradingID(rs.getString("tradingid"));
                    trade.setType(rs.getString("cardtotrade"));
                    trade.setMinDamage(rs.getInt("mindamage"));
                    trade.setCardToTrade(rs.getString("type"));
                    trades.add(trade);
                }
            }
        }catch(Exception e){
            return null;
        }
        return trades;
    }
    public Trading tradeOfferToDB(String token, Trading trading){
        String pureToken = token.substring(6);
        Integer entry = 0;
        boolean success1 = false;
        String preparedSql2 = "UPDATE aquiredpackages SET trading = true WHERE indeck = false AND id = ? AND username = (SELECT username FROM session WHERE token = ?)";
        try(PreparedStatement ps2 = conn.prepareStatement(preparedSql2)){
            ps2.setString(1, trading.getCardToTrade());
            ps2.setString(2,pureToken);
            ps2.executeUpdate();
            success1 = true;
        }catch (Exception e){

        }


        if(success1){
            String preparedSql = "INSERT INTO trades(tradingid,cardtotrade,type,mindamage,username) VALUES(?,?,?,?,(SELECT username FROM session WHERE token = ?))";
            try(PreparedStatement ps = conn.prepareStatement(preparedSql)){
                ps.setString(1,trading.getTradingID());
                ps.setString(2,trading.getCardToTrade());
                ps.setString(3,trading.getType());
                ps.setInt(4,trading.getMinDamage());
                ps.setString(5,pureToken);
                if(ps.executeUpdate()>0){
                    entry++;
                    //System.out.println(entry);
                }
            }catch (Exception e){
               // System.out.println("exception raised "+e);
            }
        }
        if (entry > 0)
            return trading;
        return null;
    }
    public Trading aquireTradeInDB(String token, String tradeID, String cardToTrade){
        String preparedSql = "SELECT * FROM trades WHERE tradingid = ?";
        Trading trade = new Trading();
        String username = "", buyer = "", type ="";
        Integer damage = 0;

        try(PreparedStatement ps = conn.prepareStatement(preparedSql)){
            ps.setString(1,tradeID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    trade.setTradingID(rs.getString("tradingid"));
                    trade.setType(rs.getString("type"));
                    trade.setMinDamage(rs.getInt("mindamage"));
                    trade.setCardToTrade(rs.getString("cardtotrade"));
                    username = rs.getString("username");
                }
            }
        }catch (Exception e){
            System.out.println("EXEX "+e);
        }


        String preparedSQL2 = "SELECT * FROM aquiredpackages WHERE id = ?";
        try(PreparedStatement ps2 = conn.prepareStatement(preparedSQL2)){
            ps2.setString(1, cardToTrade);
            try (ResultSet rs2 = ps2.executeQuery()) {
                if (rs2.next()) {
                    buyer = rs2.getString("username");
                    type = rs2.getString("name");
                    damage = rs2.getInt("damage");
                    if(type.toLowerCase().contains("spell")){
                        type = "spell";
                    }
                    else{
                        type = "monster";
                    }

                    System.out.println(buyer+ " " + type +" "+ damage );
                }
            }
        }catch (Exception e){
            System.out.println("e "+e);}

        if(!username.equals(buyer) && !buyer.equals("") && !username.equals("")){
            System.out.println("fine\n"+trade.getType().equals(type)+"\n."+type+". ."+trade.getType()+".");
            if(trade.getType().equals(type) && damage >= trade.getMinDamage()){
                System.out.println("yea");
                String preparedSql3 = "UPDATE aquiredpackages SET trading = false, seen = false, username = ? WHERE id = ?";
                String preparedSql4 = "DELETE FROM trades WHERE tradingid = ?";
                try(PreparedStatement ps3 = conn.prepareStatement(preparedSql3)){
                    ps3.setString(1, buyer);
                    ps3.setString(2,trade.getCardToTrade());
                    ps3.execute();
                    System.out.println("should update");
                    ps3.setString(1, username);
                    ps3.setString(2,cardToTrade);
                    ps3.execute();

                    try(PreparedStatement ps4 = conn.prepareStatement(preparedSql4)){
                        ps4.setString(1,tradeID);
                        ps4.execute();
                        System.out.println("should delete");
                        return trade;
                    }
                }catch (Exception e){
                    System.out.println("E "+e);
                }
            }
        }

        return null;
    }
    public String cancelTradeInDB(String token, String tradeID){
        boolean holder = false;
        String pureToken = token.substring(6);
        String preparedSql2 = "SELECT COUNT(*) FROM aquiredpackages WHERE id = (SELECT cardtotrade from trades WHERE tradingid = ?) AND username = (SELECT username FROM session WHERE token = ?)";
        String preparedSql = "DELETE FROM trades WHERE tradingid = ?";

        try(PreparedStatement ps2 = conn.prepareStatement(preparedSql2)){
        ps2.setString(1,tradeID);
        ps2.setString(2,pureToken);
        holder = ps2.execute();
        }catch (Exception e){
        }

        if(holder){
            try(PreparedStatement ps = conn.prepareStatement(preparedSql)){
                ps.setString(1, tradeID);
                ps.execute();
                    return tradeID;
            }catch (Exception e){

            }
        }

        return null;
    }

    public List<NewCards>  getNewCards(String token){
        List<NewCards> newCards = new ArrayList<>();
        String pureToken = token.substring(6);
        String preparedSql = "UPDATE aquiredpackages SET seen = true WHERE seen = false AND username = (SELECT username FROM session WHERE token = ?) RETURNING *";
        try(PreparedStatement ps = conn.prepareStatement(preparedSql)){
            ps.setString(1,pureToken);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    NewCards card = new NewCards();
                    card.setName(rs.getString("name"));
                    card.setId(rs.getString("id"));
                    card.setDamage(rs.getInt("damage"));
                    newCards.add(card);
                    System.out.println(rs.getString("name"));
                }
            }
        }catch (Exception e){
            System.out.println("exep "+e);
        }
        return newCards;
    }
}
