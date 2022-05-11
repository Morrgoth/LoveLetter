package bb.love_letter.game;

public class Princess implements Card{

    private String name = "PRINCESS";
    private int cardPoints = 8;
    private String cardAction = "Lose if discarded.";


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
