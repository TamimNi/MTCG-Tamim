package org.example.application.game.model.user;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)


public class User {
    private String username = null;
    private String name = null;

    private String password = null;

    private String bio = null;
    private String image = null;
    private int coins = 0;
    public User() {
    }
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public User(String name, String bio, String image){
        this.name = name;
        this.bio = bio;
        this.image = image;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {this.name = name;}

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getBio() {
        return bio;
    }
    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public int getCoins() {return coins;}
    public void setCoins(int coins) {
        this.coins = coins;
    }


    /*@Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }*/
}
