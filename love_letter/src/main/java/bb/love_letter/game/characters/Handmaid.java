package bb.love_letter.game.characters;

/*
    Strength: 4
    AmountInDeck: 2
    Effects: Player cannot be affected by any other player's cards until their next turn.
 */

public class Handmaid extends Cards {
    private String name = "Handmaid";
    private final int cardPoints = 4;
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

    public void useHandmaid(){


        //player1 gets immunity until amount of Clients in game, count down per turn; ;
    }

}
