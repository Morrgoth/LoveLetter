package bb.love_letter.game;

import bb.love_letter.game.characters.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class is used for initializing and shuffling the deck.
 */
public class Deck{
    ArrayList<Cards> deck = new ArrayList<>();

    public Deck() {
        initializeDeck();
        shuffleDeck();
    }

    public ArrayList<Cards> getDeck(){
        return deck;
    }

    public void initializeDeck(){
        for(int i = 0; i<5; i++){
            deck.add(new Guard());
        }

        for(int i = 0; i<2; i++){
            deck.add(new Priest());
        }

        for(int i = 0; i<2; i++){
            deck.add(new Baron());
        }

        for(int i = 0; i<2; i++){
            deck.add(new Handmaid());
        }

        for(int i = 0; i<2; i++){
            deck.add(new Prince());
        }

        deck.add(new Princess());
        deck.add(new King());
        deck.add(new Countess());
    }

    public void shuffleDeck(){
        Collections.shuffle(deck);
    }

    public Cards draw() {
        return deck.remove(0);
    }

    public int size() {
        return deck.size();
    }

    public void reset() {
        deck.clear();
        initializeDeck();
        shuffleDeck();
    }

}
