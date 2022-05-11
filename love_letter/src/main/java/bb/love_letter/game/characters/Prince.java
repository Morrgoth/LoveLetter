package bb.love_letter.game.characters;

/*
    Strength: 5
    AmountInDeck: 2
    Player may choose any player
    (including themselves) to discard their hand and draw a new one.
 */
public class Prince extends Cards implements Playable {

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

    @Override
    public int pickUp(){ /* Check if COUNTESS on hand if yes --> COUNTESS.putDown() */
        return 0;
    }

    @Override
    public int putDown(){
        //player1 chooses player2
        // swap player2 hand to player1 only;
        //if PRINCESS --> player2 terminate round;
        return 0;
    }

    @Override
    public int see() {
        //make Hand arrList visible only to player 2
        return 0;
    }
}
