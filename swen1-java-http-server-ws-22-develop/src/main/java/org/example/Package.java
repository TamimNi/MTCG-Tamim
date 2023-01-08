package org.example;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Package {
    private String Id;
    private String Name;
    private double Damage;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public double getDamage() {
        return Damage;
    }

    public void setDamage(double damage) {
        this.Damage = damage;
    }

    public static void main(String[] args) throws Exception {
        String jsonString = "[{\"Id\":\"845f0dc7-37d0-426e-994e-43fc3ac83c08\", \"Name\":\"WaterGoblin\", \"Damage\": 10.0}, {\"Id\":\"99f8f8dc-e25e-4a95-aa2c-782823f36e2a\", \"Name\":\"Dragon\", \"Damage\": 50.0}, {\"Id\":\"e85e3976-7c86-4d06-9a80-641c2019a79f\", \"Name\":\"WaterSpell\", \"Damage\": 20.0}, {\"Id\":\"1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\", \"Name\":\"Ork\", \"Damage\": 45.0}, {\"Id\":\"dfdd758f-649c-40f9-ba3a-8657f4b3439f\", \"Name\":\"FireSpell\",    \"Damage\": 25.0}]";

        ObjectMapper mapper = new ObjectMapper();
        List<Package> packages = mapper.readValue(jsonString, new TypeReference<List<Package>>(){});

        for (Package pkg : packages) {
            //System.out.println("Id: " + pkg.getId());
            //System.out.println("Name: " + pkg.getName());
            //System.out.println("Damage: " + pkg.getDamage());
        }
    }

    public void addPackage(Package aPackage) {
    }
}