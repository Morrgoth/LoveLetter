package bb.love_letter.game.characters;

import bb.love_letter.game.Game;
import bb.love_letter.game.GameEvent;
import bb.love_letter.game.Player;

/*
    Strength: 1
    AmountInDeck: 5
    Effects: Player may choose another player and name a card other than Guard.
    If the chosen player's hand contains that card, that player is eliminated from the round.
 */
public class Guard  extends Cards {
    private static String name = "Guard";
    private final int cardPoints = 1;
    private static String cardAction = "Choose a player and a specific card. If you're right and it's his in his hand, this player is terminated for the Round.";

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


    public GameEvent useGuard(Player sourcePlayer,Player chosenPlayer, String chosenCard) {
        if (chosenPlayer == null) {
            if (!chosenCard.equalsIgnoreCase("Guard")) {
                if (chosenPlayer.getCard1().getCardName().equalsIgnoreCase(chosenCard)) {
                    chosenPlayer.setInGame(false);
                    return new GameEvent(GameEvent.GameEventType.VALID_ACTION, sourcePlayer.getName() +
                            " discarded the Guard, and targeted " + chosenPlayer.getName() + " and guessed " + chosenCard
                            + "; " + chosenPlayer.getName() + " was eliminated");
                } else {
                    return new GameEvent(GameEvent.GameEventType.VALID_ACTION, sourcePlayer.getName() +
                            " discarded the Guard, and targeted " + chosenPlayer.getName() + " and guessed " + chosenCard
                            + "; " + chosenPlayer.getName() + " was not eliminated");
                }
            } else {
                return new GameEvent(GameEvent.GameEventType.INVALID_ACTION, "You can't guess Guard!", sourcePlayer);
            }
        } else {
            return new GameEvent(GameEvent.GameEventType.INVALID_ACTION, "Please provide a guess for Guard!", sourcePlayer);
        }
    }
}

