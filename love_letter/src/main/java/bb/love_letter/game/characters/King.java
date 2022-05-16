package bb.love_letter.game.characters;

import bb.love_letter.game.Player;

/*
    Strength: 6
    AmountInDeck: 1
    Effects: Player may trade hands with another player.
 */
public class King extends Cards {

    private String name = "KING";
    private final int cardPoints = 6;
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

    public GameEvent useKing(Player sourcePlayer, Player targetPlayer){
        Cards temp = targetPlayer.getCard1();
        targetPlayer.setCard1(sourcePlayer.getCard1());
        sourcePlayer.setCard1(temp);
        GameEvent swapCard = new GameEvent(sourcePlayer.getName() + " swapped the card with " + targetPlayer.getName(), GameEvent.GameEventType.KINGACTION);
        return swapCard;
        //player1 chooses player2
        // change hands;;
    }

}
