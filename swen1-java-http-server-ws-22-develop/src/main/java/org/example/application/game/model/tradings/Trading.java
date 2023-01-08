package org.example.application.game.model.tradings;
import com.fasterxml.jackson.annotation.JsonProperty;
public class Trading {
    @JsonProperty("Id")
    String tradingID = null;

    @JsonProperty("CardToTrade")
    String cardToTrade = null;
    @JsonProperty("Type")
    String type = null;
    @JsonProperty("MinimumDamage")
    Integer minDamage = 0;

    public String getTradingID() {
        return tradingID;
    }

    public void setTradingID(String tradingID) {
        this.tradingID = tradingID;
    }

    public String getCardToTrade() {
        return cardToTrade;
    }

    public void setCardToTrade(String cardToTrade) {
        this.cardToTrade = cardToTrade;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getMinDamage() {
        return minDamage;
    }

    public void setMinDamage(Integer minDamage) {
        this.minDamage = minDamage;
    }
}
