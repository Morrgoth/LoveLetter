package bb.love_letter.game.characters;

import bb.love_letter.game.*;

import java.util.ArrayList;

import static bb.love_letter.game.GameEvent.GameEventType.*;


public class Prince extends Cards{

    private final String name = "Prince";
    private final int cardPoints = 5;
    private static final String cardAction = "Choose a player. They discard their hand and draw a new card.";


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

}


