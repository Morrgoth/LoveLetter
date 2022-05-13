package bb.love_letter.game.characters;

/*
    Strength: 7
    AmountInDeck: 1
    Effects:If the player holds this card and either the King or the Prince,
    this card must be played immediately, which otherwise does nothing.
 */

public class Countess extends Cards {
    private String name = "COUNTESS";
    private int cardPoints = 7;
    private String cardAction = "Play out if either King or Prince in hand.";

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


    @Override
    public void useAction(){ /* COUNTESS does nothing on pickUp */

        /* COUNTESS checks if KING or PRINCE on pickUp
    ((implicit > if PRINCESS on hand - putDown(), but still an option to loose by PRINCESS
    COUNTESS checks every round if KING/ PRINCE in hand*/
    }

}
