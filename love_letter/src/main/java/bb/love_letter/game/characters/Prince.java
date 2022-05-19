package bb.love_letter.game.characters;

import bb.love_letter.game.*;

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


    public GameEvent usePrince(Player sourcePlayer, Player targetPlayer, Deck deck){
        targetPlayer.discardCard(1);
        if(targetPlayer.checkIfPrincess(targetPlayer.getCard1())){
            targetPlayer.setInGame(false);
            return new GameEvent(PLAYER_EFFECT, sourcePlayer.getName() + " discarded Prince on " +
                    targetPlayer.getName() + " and " + targetPlayer.getName() + " is out, because the card of  is PRINCESS.");
        }else{
            targetPlayer.addCard(deck.draw());
            return new GameEvent(PLAYER_EFFECT, "You get the card " + deck.draw().getCardName() + " from deck.", targetPlayer);
        }
    }


        //player1 chooses player2
        // swap player2 hand to player1 only;
        //if PRINCESS --> player2 terminate round;
}


