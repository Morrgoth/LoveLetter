package bb.love_letter.game.characters;

/**
 * Strength: 5
 * AmountInDeck: 2
 * Effect: Player can target another Player who has to discard their current card and draw a new one from the deck.
 *
 * @author Veronika Heckel
 * @author Philipp Keyzman
 * @author Muqiu Wang
 */
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


