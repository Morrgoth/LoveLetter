package bb.love_letter.game.characters;

/*
    Strength: 8
    AmountInDeck: 1
    If the player plays or discrds this card for any reason, they are eliminated from the round.
 */

public class Princess extends Cards implements Playable {

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

    @Override
    public int pickUp(){ /* PRINCESS does nothing on pickUp */
        return 0;
    }

    @Override
    public int putDown(){
        //player1 terminated for Round

        return 0;
    }

    @Override
    public int see() {
        //make Hand arrList visible only to player 2
        return 0;
    }
}
