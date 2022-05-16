package bb.love_letter.game;

import bb.love_letter.game.characters.Cards;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

import static bb.love_letter.game.GameApplication.history;
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
    public ArrayList<Cards> discarded = new ArrayList<>();


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
            GameApplication.history.add(card1);
            discarded.add(card1);
            setCard1(null);
            return true;
        } else if (card2.getCardName().equals("COUNTESS") && (card1.getCardName().equals("PRINCE") || card1.getCardName().equals("KING"))) {
            GameApplication.history.add(card2);
            discarded.add(card2);
            setCard2(null);
            return true;
        } else {
            return false;
        }

    }

    public boolean checkIfPrincess(Cards card){
        if(card.getCardName().equals("PRINCESS")){
            return true;
        }
        return false;
    }


    //discard a Card during each round
    public GameEvent discardCard(int cardNumber) {
        GameEvent gameEvent = new GameEvent(null, null);
        switch (cardNumber) {
            case 1:
                GameApplication.history.add(card1);
                discarded.add(card1);
                setCard1(card2);
                setCard2(null);
                gameEvent.setMessage("You chose Card 1");
                gameEvent.setType(DISCARDSUCCESSFULL);

                break;

            case 2:
                GameApplication.history.add(card2);
                discarded.add(card2);
                setCard2(null);
                gameEvent.setMessage("You chose Card 2");
                gameEvent.setType(DISCARDSUCCESSFULL);
                break;
            default:
                gameEvent.setMessage("Please enter a valid number");
                gameEvent.setType(NOSUCHCARDINHAND);
        }
        return gameEvent;
    }


    //Chose a player for cardActions
    public Player choosePlayer(int playerNumber) {
        GameEvent chosenPlayerSuccess = new GameEvent(null, null);
        GameEvent noChoice = new GameEvent(null, null);
        for (Player player : GameApplication.choosePlayer) {
            if (playerNumber == GameApplication.choosePlayer.indexOf(player)) {
                Player chosenPlayer = player;
                chosenPlayerSuccess.setMessage("You chose: " + player.name);
                chosenPlayerSuccess.setType(PLAYERCHOSEN);
                return chosenPlayer;
            } else {
                noChoice.setMessage("You can't choose that player. Choose a new one.");
                noChoice.changeState(true,INVALIDCHOICE);
            }
        }
        return null;
    }


    private void clearDiscardedList(ArrayList<Cards> discarded) {
        //delete all elements in List when a round ends
        for(int i = 0; i<discarded.size(); i++){
            discarded.remove(0);
        }
    }
}

