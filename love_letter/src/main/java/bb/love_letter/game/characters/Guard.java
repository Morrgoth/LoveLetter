package bb.love_letter.game.characters;

import bb.love_letter.game.Game;
import bb.love_letter.game.GameApplication;
import bb.love_letter.game.GameEvent;
import bb.love_letter.game.Player;

/*
    Strength: 1
    AmountInDeck: 5
    Effects: Player may choose another player and name a card other than Guard.
    If the chosen player's hand contains that card, that player is eliminated from the round.
 */
public class Guard  extends Cards {
    private String name = "Guard";
    private final int cardPoints = 1;
    private String cardAction = "Choose a player and a specific card. If you're right and it's his in his hand, this player is terminated for the Round.";

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


    public GameEvent useGuard(Player sourcePlayer,Player chosenPlayer, String chosenCard){
        //how to set player1? and how to set it automatically during the game?
        if(!chosenCard.equalsIgnoreCase("Guard")){
            if (chosenPlayer.getCard1().getCardName().equalsIgnoreCase(chosenCard)) {
                Game.playersInRound.remove(chosenPlayer);
                chosenPlayer.setInGame(false);
                return new GameEvent(GameEvent.GameEventType.PLAYER_EFFECT, sourcePlayer.getName() + " discarded GUARD on " + chosenPlayer.getName()  + " and guesses " + chosenCard +" correctly.\n" + chosenPlayer.getName() + " is out!");
            } else {
                return new GameEvent(GameEvent.GameEventType.PLAYER_EFFECT, chosenPlayer.getName() + " you guessed wrong!", sourcePlayer);
            }
        } else {
                return new GameEvent(GameEvent.GameEventType.INVALID_ACTION, "You can't guess Guard!", sourcePlayer);
        }

    }
}

