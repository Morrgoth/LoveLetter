package bb.love_letter.game.characters;

import bb.love_letter.game.Player;

public abstract class Cards {

    private String cardName;
    private int cardPoints;
    private String cardAction;

    public String getCardName(){
        return cardName;
    }

    public int getCardPoints(){
        return cardPoints;
    }

    //needed only to show the info text
    public String getCardAction(){
        return cardAction;
    }


    //when player looses::
    //
    public abstract void useAction(Player sourcePlayer, Player targetPlayer);
}
