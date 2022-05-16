package bb.love_letter.game.characters;

import bb.love_letter.game.GameApplication;
import bb.love_letter.game.GameEvent;
import bb.love_letter.game.Player;

/*
    Strength: 3
    AmountInDeck: 2
    Effects: Player may choose another player and privately compare hands.
    The player with the lower-value card is eliminated from the round.
 */
public class Baron extends Cards {
    private final String name = "Baron";
    private final int cardPoints = 3;
    private final String cardAction = "Compare Hands privately.Card with lower value is eliminated";

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
    // sourcePlayer + targetPlayer private message with Info :: You played Card+Value - target played Card + Value >> print on Server, but not in method
    // sourceCard + targetCard arrayList <Card> history add.
    // playersInRound delete user

    public void useBaron(Player sourcePlayer, Player targetPlayer) {
        /* compare hands with another player, lower number is out */
        GameEvent baronEvent = new GameEvent(GameEvent.GameEventType.PLAYERIMMUNE);
        baronEvent.changeState(GameEvent.GameEventType.BARONACTION);

        Cards sourcePlayerCard1 = sourcePlayer.getCard1();
        Cards targetPlayerCard1 = targetPlayer.getCard1();

        int sourceCardValue = sourcePlayerCard1.getCardPoints();
        int targetCardValue = targetPlayerCard1.getCardPoints();

        if (sourceCardValue > targetCardValue) {
            GameApplication.history.add(targetPlayerCard1);
            GameApplication.playersInRound.remove(targetPlayer);
            targetPlayer.setInGame(false);
            baronEvent.changeState(GameEvent.GameEventType.PLAYERELIMINATED);
        }
        else if (sourceCardValue < targetCardValue){
            GameApplication.history.add(sourcePlayerCard1);
            GameApplication.playersInRound.remove(sourcePlayer);
            sourcePlayer.setInGame(false);
            baronEvent.changeState(GameEvent.GameEventType.PLAYERELIMINATED);
        }
    }
}
