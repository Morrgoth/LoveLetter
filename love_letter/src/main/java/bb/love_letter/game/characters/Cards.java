package bb.love_letter.game.characters;

import bb.love_letter.game.Player;

import java.util.HashMap;

public abstract class Cards {

    private String cardName;
    private int cardPoints;
    private static String cardAction;

//    public static HashMap<Cards, Integer> overviewCards = new HashMap<Cards, Integer>();

    public String getCardName(){
        return cardName;
    }

    public static String getCardAction() {
        return cardAction;
    }

    public int getCardPoints(){
        return cardPoints;
    }
}

    //needed only to show the info text





