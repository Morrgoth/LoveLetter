package bb.love_letter.game;

public class King implements Card{

    private String name = "KING";
    private int cardPoints = 6;
    private String cardAction = "Trade hands with another player.";

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
