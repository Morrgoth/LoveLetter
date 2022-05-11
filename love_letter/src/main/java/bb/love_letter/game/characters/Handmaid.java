package bb.love_letter.game.characters;

/*
    Strength: 4
    AmountInDeck: 2
    Effects: Player cannot be affected by any other player's cards until their next turn.
 */

public class Handmaid extends Cards implements Playable {
    private String name = "HANDMAID";
    private int cardPoints = 4;
    private String cardAction = "This card grants you immunity until your next turn.";

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
    public int pickUp(){ /* HANDMAID does nothing on pickUp */
        return 0;
    }

    @Override
    public int putDown(){
        //player1 gets immunity until amount of Clients in game, count down per turn;
        return 0;
    }

    @Override
    public int see(){
        //make Hand arrList visible only to player 2
        return 0;
    }
}
