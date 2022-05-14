package bb.love_letter.game.characters;

/*
    Strength: 8
    AmountInDeck: 1
    If the player plays or discrds this card for any reason, they are eliminated from the round.
 */

public class Princess extends Cards{

    private final String name = "PRINCESS";
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
