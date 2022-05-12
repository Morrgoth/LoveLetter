package bb.love_letter.game;

import bb.love_letter.game.characters.*;
import java.util.ArrayList;
import java.util.Collections;

public class Deck{
    ArrayList<Cards> deck = new ArrayList<>();

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

}