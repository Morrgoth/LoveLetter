package bb.love_letter.game.characters;

/*
    Strength: 6
    AmountInDeck: 1
    Effects: Player may trade hands with another player.
 */
public class King extends Cards implements Playable {

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
    public int pickUp(){ /* KING checks if COUNTESS is in players Hand, if yes trigger COUNTESS effect and putDown(); */
        return 0;
    }

    @Override
    public int putDown(){
        //player1 chooses player2
        // change hands;
        return 0;
    }

    @Override
    public int see() {
        //make Hand arrList visible only to player 2
        return 0;
    }
}
