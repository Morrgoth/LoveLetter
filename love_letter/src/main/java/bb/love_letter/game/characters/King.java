package bb.love_letter.game.characters;

import bb.love_letter.game.Game;
import bb.love_letter.game.GameEvent;
import bb.love_letter.game.Player;

/**
 * Strength: 6
 * AmountInDeck: 1
 * Effects: Player may trade hands with another player.
 *
 * @author Veronika Heckel
 * @author Philipp Keyzman
 * @author Muqiu Wang
 */
public class King extends Cards {

    private String name = "King";
    private final int cardPoints = 6;
    private static String cardAction = "Trade hands with another player.";
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
    public GameEvent useKing(Player sourcePlayer, Player targetPlayer){
        Cards temp = targetPlayer.getCard1();
        targetPlayer.setCard1(sourcePlayer.getCard1());
        sourcePlayer.setCard1(temp);
        return new GameEvent(GameEvent.GameEventType.VALID_ACTION, sourcePlayer.getName() +
                " discarded the King and targeted " + targetPlayer.getName());

    }
}
