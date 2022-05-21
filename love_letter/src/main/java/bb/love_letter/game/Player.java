package bb.love_letter.game;

import bb.love_letter.game.characters.Cards;

import java.util.ArrayList;

/**
 * Player extends class User. Contains all attributes relevant to the Game.
 *
 * @author Veronika Heckel
 * @author Muqiu Wang
 * @author Philipp Keyzman
 */
public class Player extends User {
    private Cards card1;
    private Cards card2;
    private boolean inGame;
    private boolean immune;
    private int score;
    public ArrayList<Cards> discarded = new ArrayList<>();
    public Player(String name, Cards card1, Cards card2) {
        super(name);
        this.card1 = card1;
        this.card2 = card2;
    }
    public Player(User user) {
        super(user.getName());
    }
    public Cards getCard1() {
        return card1;
    }
    public void setCard1(Cards card1) {
        this.card1 = card1;
    }
    public Cards getCard2() {
        return card2;
    }
    public void setCard2(Cards card2) {
        this.card2 = card2;
    }

    /**
     * Adds a new card to the first available card slot of the Player.
     * @param card The new card to be added to the Player's hand.
     */
    public void addCard(Cards card) {
        if (card1 == null) {
            setCard1(card);
        } else {
            setCard2(card);
        }
    }
    public void setImmune(boolean immune) {
        this.immune = immune;
    }
    public boolean getInGame() {
        return inGame;
    }
    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }
    public ArrayList<Cards> getDiscarded() {
        return discarded;
    }
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Generates a String with the current cards of the Player in hand.
     * @return
     */
    public String printHand() {
        StringBuilder message = new StringBuilder();
        if (card1 != null) {
            message.append("1: ").append(card1.getCardName()).append("\n");
        }
        if (card2 != null) {
            message.append("2: ").append(card2.getCardName()).append("\n");
        }
        return message.toString();
    }
    public boolean isImmune() {
        return immune;
    }
    public void eliminate() {
        setInGame(false);
    }
}
