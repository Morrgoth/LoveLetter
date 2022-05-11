package bb.love_letter.game;

public class Baron implements Card{
    private String cardName = "BARON";
    private String cardAction = "You can secretly compare hands with an another player. \nThe player who has less points is out this round.";
    private int cardPoints = 3;


    @Override
    public String getCardName() {
        return this.cardName;
    }

    @Override
    public String getCardAction() {
        return this.cardAction;
    }

    @Override
    public int getCardPoints() {
        return this.cardPoints;
    }
}
