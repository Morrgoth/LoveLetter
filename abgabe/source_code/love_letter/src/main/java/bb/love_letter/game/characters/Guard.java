package bb.love_letter.game.characters;

import bb.love_letter.game.Game;
import bb.love_letter.game.GameEvent;
import bb.love_letter.game.Player;

/**
 * Strength: 1
 * AmountInDeck: 5
 * Effects: Player may choose another player and name a card other than Guard.
 *     If the chosen player's hand contains that card, that player is eliminated from the round.
 *
 * @author Veronika Heckel
 * @author Philipp Keyzman
 * @author Muqiu Wang
 * @author Bence Ament
 */
public class Guard extends Cards {
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

    /**
     * Checks if the player tries using the Guard card correctly (Guard cannot be guessed).
     * @param sourcePlayer The player who discarded Guard
     * @param chosenPlayer The targeted player
     * @param chosenCard The guess of sourcePlayer. It is not case-sensitive.
     * @return
     */
    public GameEvent useGuard(Player sourcePlayer,Player chosenPlayer, String chosenCard) {
        if (chosenCard != null) {
            if (!chosenCard.equalsIgnoreCase("Guard")) {
                if (isValidGuess(chosenCard)) {
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
                    return new GameEvent(GameEvent.GameEventType.INVALID_ACTION, "There is no card called: " + chosenCard, sourcePlayer);
                }
            } else {
                return new GameEvent(GameEvent.GameEventType.INVALID_ACTION, "You can't guess Guard!", sourcePlayer);
            }
        } else {
            return new GameEvent(GameEvent.GameEventType.INVALID_ACTION, "Please provide a guess for Guard!", sourcePlayer);
        }
    }

    /**
     * Checks if the player provided a valid guess as Card name.
     * @param guess The guess inputted by the user
     * @return
     */
    private boolean isValidGuess(String guess) {
        if (guess.equalsIgnoreCase("Priest")) {
            return true;
        } else if (guess.equalsIgnoreCase("Baron")) {
            return true;
        } else if (guess.equalsIgnoreCase("Handmaid")) {
            return true;
        } else if (guess.equalsIgnoreCase("Prince")) {
            return true;
        } else if (guess.equalsIgnoreCase("King")) {
            return true;
        } else if (guess.equalsIgnoreCase("Countess")) {
            return true;
        }  else if (guess.equalsIgnoreCase("Princess")) {
            return true;
        } else {
            return false;
        }
    }
}

