package bb.love_letter.game.characters;

/**
 * @author Veronika Heckel
 * @author Philipp Keyzman
 * @author Muqiu Wang
 */
public abstract class Cards {
    private String cardName;
    private int cardPoints;
    private static String cardAction;
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





