package bb.love_letter.game.characters;

/*
    Strength: 7
    AmountInDeck: 1
    Effects:If the player holds this card and either the King or the Prince,
    this card must be played immediately, which otherwise does nothing.
 */

public class Countess extends Cards implements Playable {
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
    public int pickUp(){ /* COUNTESS checks if KING or PRINCE on pickUp
    ((implicit > if PRINCESS on hand - putDown(), but still an option to loose by PRINCESS
    COUNTESS checks every round if KING/ PRINCE in hand*/
    return 0;
    }

    @Override
    public int putDown(){
        //nothing really
        return 0;
    }

    @Override
    public int see(){
        //make Hand arrList visible only to player 2
        return 0;
    }


}
