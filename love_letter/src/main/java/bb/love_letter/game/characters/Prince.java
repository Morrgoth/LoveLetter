package bb.love_letter.game.characters;

/*
    Strength: 5
    AmountInDeck: 2
    Player may choose any player
    (including themselves) to discard their hand and draw a new one.
 */
public class Prince extends Cards{

    private final String name = "PRINCE";
    private final int cardPoints = 5;
    private final String cardAction = "Choose a player. They discard their hand and draw a new card.";


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
    public void useAction(){ /* BARON does nothing on pickUp */

        //player1 chooses player2
        // swap player2 hand to player1 only;
        //if PRINCESS --> player2 terminate round;
    }

}
