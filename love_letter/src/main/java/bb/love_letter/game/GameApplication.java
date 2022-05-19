package bb.love_letter.game;

import bb.love_letter.game.characters.Cards;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import static bb.love_letter.game.GameEvent.GameEventType.*;

public class GameApplication {





    public void addPlayers(User user){
        while(playersInGame.size()<=4){
            if(!playersInGame.contains(user))
                playersInGame.add(new Player(user.getName(), null, null));
        }
    }

    public void initializePlayerScores(){
        for(int i = 0; i< playersInGame.size();i++){
            playerScores.put(playersInGame.get(i).getName(), 0);
        }
    }

    public void initializePlayersInRound(){
        playersInRound = (ArrayList<Player>) playersInGame.clone();
    }

    public void startGame(){

        int i = 0;
        if(playersInGame.size() >= 2){
            Deck deck = new Deck();
            //Do the Initialization and shuffling for the deck
            deck.initializeDeck();
            deck.shuffleDeck();
            //initializePlayersInRound();
            initializePlayerScores();

        }else{
            GameEvent lackOfPlayer = new GameEvent(GameEvent.GameEventType.PLAYERIMMUNE);
        }
    }

    public void startRound(){

        for (int i = 0; i < history.size(); i++){
            history.remove(0);
        }
        Deck deck = new Deck();
        //Do the Initialization and shuffling for the deck
        deck.initializeDeck();
        deck.shuffleDeck();
    }

}
