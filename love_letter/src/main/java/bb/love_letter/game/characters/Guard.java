package bb.love_letter.game.characters;

/*
    Strength: 1
    AmountInDeck: 5
    Effects: Player may choose another player and name a card other than Guard.
    If the chosen player's hand contains that card, that player is eliminated from the round.
 */
public class Guard  extends Cards implements Playable {
    private String name = "BARON";
    private int cardPoints = 1;
    private String cardAction = "Choose a player and a specific card. If you're right and it's his in his hand, this player is terminated for the Round.";

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
        //compare value.name, if correct
        //terminate Round for player;
        //else do nothing;
        return 0;
    }

    @Override
    public int see(){
        //make Hand arrList visible only to player 2
        return 0;
    }


}
