package bb.love_letter.game.characters;

import bb.love_letter.game.Game;
import bb.love_letter.game.GameEvent;
import bb.love_letter.game.Player;

import static bb.love_letter.game.Game.playerQueue;

/*
    Strength: 3
    AmountInDeck: 2
    Effects: Player may choose another player and privately compare hands.
    The player with the lower-value card is eliminated from the round.
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

    //when player looses::
    // sourcePlayer + targetPlayer private message with Info :: You played Card+Value - target played Card + Value >> print on Server, but not in method
    // sourceCard + targetCard arrayList <Card> history add.
    // playersInRound delete user

    public GameEvent useBaron(Player sourcePlayer, Player targetPlayer) {
        /* compare hands with another player, lower number is out */

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
        else if (sourceCardValue < targetCardValue){
            Game.history.add(sourcePlayerCard1);
            sourcePlayer.setInGame(false);
            return new GameEvent(GameEvent.GameEventType.CARD_EFFECT, sourcePlayer.getName() +
                    " discarded the Baron, and targeted " + targetPlayer.getName() + "; " + sourcePlayer.getName() + " was eliminated");
        }else{
            return new GameEvent(GameEvent.GameEventType.CARD_EFFECT, "Nothing happened.");
        }
    }
}
