package bb.love_letter.game.characters;

import bb.love_letter.game.GameEvent;
import bb.love_letter.game.Player;

import static bb.love_letter.game.GameEvent.GameEventType.*;

/**
 * Strength: 2
 * AmountInDeck: 2
 * Player may privately see another player's hand.
 *
 * @author Veronika Heckel
 * @author Philipp Keyzman
 * @author Muqiu Wang
 */
public class Priest extends Cards{

    private String name = "Priest";
    private final int cardPoints = 2;
    private static String cardAction = "Look at a player's hand in private.";
    @Override
    public String getCardName() {
        return name;
    }
    public static String getCardAction() {
        return cardAction;
    }
    @Override
    public int getCardPoints() {
        return cardPoints;
    }
    public GameEvent usePriest(Player sourcePlayer, Player targetPlayer){
         return new GameEvent(CARD_EFFECT,  targetPlayer.getName() + " has a " +
                targetPlayer.getCard1().getCardName(), sourcePlayer);
    }
}
