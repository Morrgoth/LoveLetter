package bb.love_letter.game.characters;

/*
    Strength: 2
    AmountInDeck: 2
    Player may privately see another player's hand.
 */

public class Priest extends Cards implements Playable {

    private String name = "PRIEST";
    private int cardPoints = 2;
    private String cardAction = "Look at a player's hand in private.";

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
    public int pickUp(){ /* nohing */
        return 0;
    }

    @Override
    public int putDown(){
        //player1 chooses player2
        // see()player2 hand to player1 only;
        return 0;
    }

    @Override
    public int see() {
        //make Hand arrList visible only to player 2
        return 0;
    }
}
