package bb.love_letter.game;

public class GameAction {

    private int cardIndex;
    private String target;
    private String guess;

    public GameAction(int cardIndex) {
        this.cardIndex = cardIndex;
    }

    public GameAction(int cardIndex, String target) {
        this.cardIndex = cardIndex;
        this.target = target;
    }

    public GameAction(int cardIndex, String target, String guess) {
        this.cardIndex = cardIndex;
        this.target = target;
        this.guess = guess;
    }

    public int getCardIndex() {
        return cardIndex;
    }

    public void setCardIndex(int cardIndex) {
        this.cardIndex = cardIndex;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getGuess() {
        return guess;
    }

    public void setGuess(String guess) {
        this.guess = guess;
    }
}
