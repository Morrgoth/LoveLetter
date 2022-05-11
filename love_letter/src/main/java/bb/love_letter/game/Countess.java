package bb.love_letter.game;

public class Countess implements Card{
    private String cardName = "COUNTESS";
    private String cardAction = "You must discard this card when you have prince or king at the same time.";
    private int cardPoints = 7;


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
