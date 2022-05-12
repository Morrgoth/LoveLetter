package bb.love_letter.game.characters;


/*
    Strength: 3
    AmountInDeck: 2
    Effects: Player may choose another player and privately compare hands.
    The player with the lower-value card is eliminated from the round.
 */
public class Baron extends Cards implements Playable {
    private String name = "BARON";
    private int cardPoints = 3;
    private String cardAction = "Compare Hands privately.Lower value card is eliminated";

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
    public int pickUp(){ /* BARON does nothing on pickUp */
    return 0;
    }

    @Override
    public int putDown(){
        //player1 chooses player2 to compare Card on Hand;
        //compare values --> higher value stays, lower value is terminated for this round
        //terminate Round for player;
        return 0;
    }

    @Override
    public int see(){
        //make Hand arrList visible only to player 2
        return 0;
    }


}
