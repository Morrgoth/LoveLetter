package bb.love_letter.game.characters;

/*
    Strength: 8
    AmountInDeck: 1
    If the player plays or discards this card for any reason, they are eliminated from the round.
 */

import bb.love_letter.game.GameApplication;

public class Princess extends Cards{

    private final String name = "Princess";
    private final int cardPoints = 8;
    private final String cardAction = "Lose if discarded.";


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

}
