/*package org.example;
import org.example.application.game.model.deck.Deck;
import org.example.application.game.model.packages.Packages;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class db {
    private static String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static String DB_USER = "postgres";
    private static String DB_PW = "postgres";
    public static Connection conn;

    public static void connect() throws SQLException {
        conn = DriverManager.getConnection(
                DB_URL, DB_USER, DB_PW
        );
    }

    public static void userToDb(String username, String password) throws SQLException {
        String preparedSql = "INSERT INTO users(username, password) VALUES(?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(preparedSql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.execute();
        }
    }

    public static String passwordFromDb(String username) throws SQLException {
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

    public static void tokenToDB(String username, String token) throws SQLException {

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

    public static void packagesToDB(List<Packages> packages, String token) throws SQLException {
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
        }

        if (isAdmin) {
            for (Packages pkg : packages) {
                try (PreparedStatement ps = conn.prepareStatement(preparedSql)) {
                    //ps.setString(1, token);
                    ps.setString(1, pkg.getId());
                    ps.setString(2, pkg.getName());
                    ps.setDouble(3, pkg.getDamage());
                    ps.execute();
                }
            }
        }
    }

    public static List<Map<String, Object>> aquirePackageFromDB(String token) throws SQLException {
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

    public static List<Map<String, Object>> aquiredCardsFromDB(String token) throws SQLException {
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
        }
        return list;
    }

    public static List<Map<String, Object>> deckCardsToDB(List<Deck> deck, String token) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<>();

        //String preparedSql3 = "UPDATE aquiredpackages SET indeck = true WHERE username = (SELECT username FROM session WHERE token = ?) RETURNING *;";
        //String preparedSql3 = "UPDATE aquiredpackages SET indeck = true WHERE username = (SELECT username FROM session WHERE token = ?) AND id IN (?, ?, ?, ?) AND (SELECT COUNT(*) FROM aquiredpackages WHERE indeck = true AND username = (SELECT username FROM session WHERE token = ?)) <= 4 RETURNING *;";
        String preparedSql3 = "UPDATE aquiredpackages\n" +
                "SET indeck = true\n" +
                "WHERE username = (SELECT username FROM session WHERE token = ?)\n" +
                "AND (SELECT COUNT(*) FROM aquiredpackages WHERE indeck = true AND username = (SELECT username FROM session WHERE token = ?)) = 0\n" +
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

    public static List<Map<String, Object>> deckCardsFromDB(String token) throws SQLException {
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

    public static List<Map<String, Object>> userFromDB(String token, String username) throws SQLException {
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

    public static List<Map<String, Object>> editUserInDB(String token, String username, String name, String bio, String image) throws SQLException {
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
        return db.userFromDB(token, username);
    }

    public static Map<String, Object> getStatsOfUser(String token) throws SQLException {
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
    public static List<Map<String, Object>> getScoreboardFromDb(String token) throws SQLException {
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
                    System.out.println("query "+rs.getString("username"));
                }
            }
        }
        return list;
    }
}*/
