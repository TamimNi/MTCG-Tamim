package org.example.application.game.model.scoreboard;
import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Scoreboard {
    private int elo;
    private String username;
    private String image;




    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public int getElo() {
        return elo;
    }
    public void setElo(int elo) {
        this.elo = elo;
    }
}
