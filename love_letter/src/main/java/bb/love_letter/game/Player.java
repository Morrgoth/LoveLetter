package bb.love_letter.game;

import bb.love_letter.game.characters.Cards;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

import static bb.love_letter.game.Game.history;
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

    public void addCard(Cards card) {
        if (card1 == null) {
            setCard1(card);
        } else {
            setCard2(card);
        }
    }

    public boolean getImmune() {
        return getImmune();
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

    public int discardedPoints(ArrayList<Cards> discarded) {
        int sum = 0;
        for (Cards card : discarded) {
            sum += card.getCardPoints();
        }
        return sum;
    }

    //player's action - draw the top most  card from deck
    public void drawCard(Deck deck) {
        card2 = deck.getDeck().get(0);
    }

    public boolean checkIfCountess(Cards card1, Cards card2) {

        //check for COUNTESS
        if (card1.getCardName().equals("COUNTESS") && (card2.getCardName().equals("PRINCE") || card2.getCardName().equals("KING"))) {
            Game.history.add(card1);
            discarded.add(card1);
            setCard1(null);
            return true;
        } else if (card2.getCardName().equals("COUNTESS") && (card1.getCardName().equals("PRINCE") || card1.getCardName().equals("KING"))) {
            Game.history.add(card2);
            discarded.add(card2);
            setCard2(null);
            return true;
        } else {
            return false;
        }

    }

    public boolean checkIfPrincess(Cards card) {
        if (card.getCardName().equals("PRINCESS")) {
            return true;
        }
        return false;
    }


    //discard a Card during each round
    public GameEvent discardCard(int cardNumber) {
        GameEvent gameEvent = new GameEvent(null, null);
        switch (cardNumber) {
            case 1:
                Game.history.add(card1);
                discarded.add(card1);
                setCard1(card2);
                setCard2(null);
                return new GameEvent(GameEvent.GameEventType.DISCARDSUCCESSFULL,"You chose Card 1");
            case 2:
                Game.history.add(card2);
                discarded.add(card2);
                setCard2(null);
                return new GameEvent(GameEvent.GameEventType.DISCARDSUCCESSFULL,"You chose Card 2");
            default:
                return new GameEvent(GameEvent.GameEventType.NOSUCHCARDINHAND,"Please enter a valid number");
        }

    }

    private void clearDiscardedList (ArrayList < Cards > discarded) {
        //delete all elements in List when a round end
        for (int i = 0; i < discarded.size(); i++) {
            discarded.remove(0);
        }
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String printHand() {
        return "1: " + card1.getCardName() + "\n2: " + card2.getCardName();
    }

    public boolean isImmune() {
        return immune;
    }

    public void eliminate() {
        setInGame(false);
    }
}
