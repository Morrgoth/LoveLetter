package bb.love_letter.game.characters;

/*
    Strength: 8
    AmountInDeck: 1
    If the player plays or discards this card for any reason, they are eliminated from the round.
 */

public class Princess extends Cards{

    private final String name = "Princess";
    private final int cardPoints = 8;
    private static final String cardAction = "Lose if discarded.";


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
