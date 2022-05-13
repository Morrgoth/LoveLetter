package bb.love_letter.game.characters;


import bb.love_letter.game.Player;

/*
    Strength: 3
    AmountInDeck: 2
    Effects: Player may choose another player and privately compare hands.
    The player with the lower-value card is eliminated from the round.
 */
public class Baron extends Cards {
    private final String name = "BARON";
    private final int cardPoints = 3;
    private final String cardAction = "Compare Hands privately.Lower value card is eliminated";

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

    //when player looses::
    //
    @Override
    public void useAction(Player sourcePlayer, Player targetPlayer) {
        /* compare hands with another player, lower number is out */
        Cards sourcePlayerCard1 = sourcePlayer.getCard1();
        Cards targetPlayerCard1 = targetPlayer.getCard1();

        sourcePlayerCard1.getCardPoints();

        if (sourcePlayerCard1 > targetPlayerCard1) {

        }
        else if (sourcePlayerCard1 < targetPlayerCard1){

        }

    }
}
