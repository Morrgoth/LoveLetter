package bb.love_letter.game.characters;

import bb.love_letter.game.GameEvent;

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

    public void usePrince(){
        GameEvent princeEvent = new GameEvent(PRINCEACTION);

        //player1 chooses player2
        // swap player2 hand to player1 only;
        //if PRINCESS --> player2 terminate round;
    }

}
