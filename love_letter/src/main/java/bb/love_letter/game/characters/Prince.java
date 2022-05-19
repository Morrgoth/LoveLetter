package bb.love_letter.game.characters;

import bb.love_letter.game.Deck;
import bb.love_letter.game.GameApplication;
import bb.love_letter.game.GameEvent;
import bb.love_letter.game.Player;

import java.util.ArrayList;

import static bb.love_letter.game.GameEvent.GameEventType.*;


public class Prince extends Cards{

    private final String name = "PRINCE";
    private final int cardPoints = 5;
    private final String cardAction = "Choose a player. They discard their hand and draw a new card.";


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


    public GameEvent usePrince(Player targetPlayer, Deck deck){
        GameApplication.history.add(targetPlayer.getCard1());
        targetPlayer.discarded.add(targetPlayer.getCard1());
        targetPlayer.setCard1(deck.getDeck().get(0));
        GameEvent princeEvent = new GameEvent(PLAYER_EFFECT);
        if(targetPlayer.checkIfPrincess(targetPlayer.getCard1())) {
            targetPlayer.setInGame(false);
            GameEvent eliminated = new GameEvent(PLAYERELIMINATED);
            return eliminated;
        }else if(targetPlayer.checkIfPrincess(targetPlayer.getCard1())){
                GameEvent princessEvent = new GameEvent(PLAYER_EFFECT);
                return princessEvent;
            }

        return princeEvent;
    }


        //player1 chooses player2
        // swap player2 hand to player1 only;
        //if PRINCESS --> player2 terminate round;
}


