package bb.love_letter.game;

public class Handmaid implements Card{
    private String cardName = "HANDMAID";
    private String cardAction = "All actions from other players are invalid to you til next turn.";
    private int cardPoints = 4;


    @Override
    public String getCardName() {
        return this.cardName;
    }

    @Override
    public String getCardAction() {
        return this.cardAction;
    }

    @Override
    public int getCardPoints() {
        return this.cardPoints;
    }
}
