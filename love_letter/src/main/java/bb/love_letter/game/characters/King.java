package bb.love_letter.game.characters;

/*
    Strength: 6
    AmountInDeck: 1
    Effects: Player may trade hands with another player.
 */
public class King extends Cards {

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

    @Override
    public void useAction(){ /* BARON does nothing on pickUp */

        //player1 chooses player2
        // change hands;;
    }

}
