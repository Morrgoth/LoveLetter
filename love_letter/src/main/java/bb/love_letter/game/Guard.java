package bb.love_letter.game;

public class Guard implements Card{
    private String cardName = "GUARD";
    private String cardAction = "Guess a non-Guard card from an another player.\nThe player ist out this round, when correctly guessed.";
    private int cardPoints = 1;

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
