package bb.love_letter.game;

public class Priest implements Card{

    private String name = "PRIEST";
    private int cardPoints = 2;
    private String cardAction = "Look at a player's hand.";

    @Override
    public String getCardName() {
        return name;
    }

    @Override
    public String getCardAction() {
        return cardAction;
    }

    @Override
    public int getCardPoints() {
        return cardPoints;
    }
}
