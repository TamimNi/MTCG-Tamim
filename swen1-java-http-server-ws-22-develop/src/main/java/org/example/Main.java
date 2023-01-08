package org.example;

import org.example.application.demo.DemoApp;
import org.example.application.game.GameApp;
import org.example.application.housing.HousingApp;
import org.example.application.socialmedia.SocialMediaApp;
import org.example.application.waiting.WaitingApp;
import org.example.server.Server;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        Server server = new Server(new GameApp());
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}