package bb.love_letter.game.characters;


/**
 * Strength: 7
 * AmountInDeck: 1
 * Effects:If the player holds this card and either the King or the Prince,
 *      this card must be played immediately, which otherwise does nothing.
 *
 * @author Veronika Heckel
 * @author Philipp Keyzman
 * @author Muqiu Wang
 */
public class Countess extends Cards {
    private String name = "Countess";
    private final int cardPoints = 7;
    private static String cardAction = "Play out if either King or Prince in hand.";
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
