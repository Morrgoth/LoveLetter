package bb.love_letter.game.characters;

import bb.love_letter.game.Game;
import bb.love_letter.game.GameEvent;
import bb.love_letter.game.Player;


/**
 * Strength: 3
 * AmountInDeck: 2
 * Effects: Player may choose another player and privately compare hands.
 * The player with the lower-value card is eliminated from the round.
 *
 * @author Veronika Heckel
 * @author Philipp Keyzman
 * @author Muqiu Wang
 */
public class Baron extends Cards {
    private final String name = "Baron";
    private final int cardPoints = 3;
    private static final String cardAction = "Compare Hands privately.Card with lower value is eliminated";
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
    public GameEvent useBaron(Player sourcePlayer, Player targetPlayer) {
        Cards sourcePlayerCard1 = sourcePlayer.getCard1();
        Cards targetPlayerCard1 = targetPlayer.getCard1();

        int sourceCardValue = sourcePlayerCard1.getCardPoints();
        int targetCardValue = targetPlayerCard1.getCardPoints();

        if (sourceCardValue > targetCardValue) {
            Game.history.add(targetPlayerCard1);
            targetPlayer.setInGame(false);
            return new GameEvent(GameEvent.GameEventType.VALID_ACTION, sourcePlayer.getName() +
                    " discarded the Baron, and targeted " + targetPlayer.getName() + "; " + targetPlayer.getName() + " was eliminated");
        }
        else if(sourceCardValue < targetCardValue){
            Game.history.add(sourcePlayerCard1);
            sourcePlayer.setInGame(false);
            return new GameEvent(GameEvent.GameEventType.VALID_ACTION, sourcePlayer.getName() +
                    " discarded the Baron, and targeted " + targetPlayer.getName() + "; " + sourcePlayer.getName() + " was eliminated");
        }else{
            return new GameEvent(GameEvent.GameEventType.VALID_ACTION, "Nothing happened.");
        }
    }
}
