package bb.love_letter.game.characters;

/*
    Strength: 2
    AmountInDeck: 2
    Player may privately see another player's hand.
 */

import bb.love_letter.game.GameApplication;
import bb.love_letter.game.GameEvent;
import bb.love_letter.game.Player;

import static bb.love_letter.game.GameEvent.GameEventType.*;

public class Priest extends Cards{

    private String name = "PRIEST";
    private final int cardPoints = 2;
    private String cardAction = "Look at a player's hand in private.";

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

    public GameEvent usePriest(Player sourcePlayer, Player targetPlayer){
        GameEvent showCard = new GameEvent(PLAYER_EFFECT, "The hand of " + targetPlayer.getName() + " is " +
                targetPlayer.getCard1().getCardName() + ".", sourcePlayer);
        return showCard;
    }
}
