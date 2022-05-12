package bb.love_letter.game;


import bb.love_letter.game.characters.Cards;

import java.util.ArrayList;

public class GameApplication {

    Deck deck = new Deck();
   //List of players in Game
    ArrayList<Player> players = new ArrayList<>();
    ArrayList<Cards> gameHistory = new ArrayList<>();
    boolean gameOver;


    public GameApplication(){
        players.add(new Player("Muqiu", null, null, 0));
        players.add(new Player("Veronika", null, null, 0));
        gameOver = false;
        deck.initializeDeck();
        deck.shuffleDeck();
        withdrawCardsAtBeginning();

        }


    //Takes the right number of Cards from Deck at the Beginning of each round
    public void withdrawCardsAtBeginning(){
        if(players.size() == 2){
            deck.getDeck().remove(0);
            for(int i = 0; i < 3; i++){
                deck.getDeck().get(i).getCardName();
                gameHistory.add(deck.getDeck().get(i));
                deck.getDeck().remove(i);
            }
        }else{
            deck.getDeck().remove(0);
            gameHistory.add(deck.getDeck().get(0));
    }

    }
}
