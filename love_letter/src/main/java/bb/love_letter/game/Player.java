package bb.love_letter.game;

import bb.love_letter.game.characters.Cards;

import java.util.ArrayList;

import static bb.love_letter.game.GameEvent.GameEventType.*;



/**
 * class Player extends class User. Contains all attributes and actions that a player can make during the game
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

    private int score = 0;
    public ArrayList<Cards> discarded = new ArrayList<>();

    public Player(User user) {
        super(user.getName());
    }

    public Player(String name, Cards card1, Cards card2) {
        super(name);
        this.card1 = card1;
        this.card2 = card2;
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



    public String printHand() {
        return "1: " + card1.getCardName() + "\n2: " + card2.getCardName();
    }

    public boolean getImmune() {
        return getImmune();
    }

    public void setImmune(boolean immune) {
        this.immune = immune;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
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















}
