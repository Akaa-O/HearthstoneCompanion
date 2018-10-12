package akaa.com.hearthstonecompanion.model;

import com.google.gson.annotations.SerializedName;

public class Card {

    @SerializedName("img")
    private String cardImagePath = null;

    @SerializedName("name")
    private String cardName = null;

    @SerializedName("playerClass")
    private String cardClass = null;

    @SerializedName("type")
    private String cardType = null;

    public Card (String cImage, String cName, String cType, String cClass) {
        cardImagePath = cImage;
        cardName = cName;
        cardType = cType;
        cardClass = cClass;
    }

    public String getCardImagePath(){
        return cardImagePath;
    }

    public String getCardName(){
        return cardName;
    }

    public String getCardClass(){
        return cardClass;
    }

    public String getCardType () {
        return cardType;
    }

}
