package akaa.com.hearthstonecompanion.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CardResponse {

    @SerializedName("Classic")
    private List<Card> cards;

    public List<Card> getCards() {
        return cards;
    }
}
