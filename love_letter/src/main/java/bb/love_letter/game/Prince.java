package bb.love_letter.game;

public class Prince implements Card {

    private String name = "PRINCE";
    private int cardPoints = 5;
    private String cardAction = "Choose a player. They discard their hand and draw a new card.";


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
