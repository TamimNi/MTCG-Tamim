package org.example.application.game.model.deck;
import com.fasterxml.jackson.annotation.JsonFormat;
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Deck {
    private String id = null;
    private String name = null;
    private double damage = 0;
    public Deck(String id) {
        this.id = id;
    }
    public Deck(){}
    public String getId() { return id; }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    @Override
    public String toString(){
        String allValues = "";
        allValues = "\n" +
                "id: " + id + "\n" +
                "name: " + name + "\n" +
                "damage" + damage + "\n";
        return allValues;
    }
}
