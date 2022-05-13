package bb.love_letter.game;

import bb.love_letter.game.characters.Cards;
import bb.love_letter.game.characters.GameEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

import static bb.love_letter.game.characters.GameEvent.GameEventType.*;

public class GameApplication {
    static ArrayList<Player> players = new ArrayList<>();

    static HashMap<String, Integer> playerScores = new HashMap<String, Integer>();
    static ArrayList<Cards> history;

    public Player winner = null;



    public void addPlayers(User user){
        while(players.size()<4){
            if(!players.contains(user))
                players.add(new Player(user.getName(), null, null));
        }
    }

    public void initializePlayerScores(){
        for(int i = 0; i< players.size();i++){
            playerScores.put(players.get(i).getName(), 0);
        }
    }

    public ArrayList<GameEvent> startGame(){
        ArrayList<GameEvent> gameEvents = new ArrayList<>();
        int i = 0;
        if(players.size() > 2){
            Deck deck = new Deck();
            //Do the Initialization and shuffling for the deck
            deck.initializeDeck();
            deck.shuffleDeck();

            gameEvents.add(withdrawFirstCards(deck));
        }else{
            GameEvent lackOfPlayer = new GameEvent("Lack of Player!", GAMEISREADY);
        }
        return gameEvents;
    }

    private GameEvent withdrawFirstCards(Deck deck){
        GameEvent broadcast = new GameEvent(null, null);
        //When there are 2 players, 4 cards out of the deck and last 3 cards from them should be seen by all
        if(players.size() == 2){
            for(int i = 0; i<4; i++){
                if(i > 0){
                    //Print out to all, which last 3 cards are removed from deck
                    String msg = "Card " + deck.getDeck().get(i).getCardName() + "removed.";
                    broadcast.setMessage(msg);
                    broadcast.setGameEvent(GAMEISREADY);
                }
                //The removed card is added to the history
                history.add(deck.getDeck().get(0));
                deck.getDeck().remove(0);
            }
        }//When there are more than 2 players, only 1 card is out, and it shouldn't be seen
        else if(players.size() == 3 || players.size() == 4){
            history.add(deck.getDeck().get(0));
            deck.getDeck().remove(0);

        }

        //Players get the first card in the beginning
        for(int i = 0; i< players.size(); i++){
            players.get(i).setCard1(deck.getDeck().get(0));
            deck.getDeck().remove(0);
        }
        return broadcast;
    }



    public static void main(String[] args) {





        //This list 'players' list is only an example for testing
        players.add(new Player("Muqiu", null, null));
        players.add(new Player("Veronika", null, null));

        Deck deck = new Deck();




        //Players get the second card in their turns and discard cards in the order of the list 'players'
        int i = 0;
        while(i< players.size()){
            //create a current player object to save the information of the player in turn
            Player currentPlayer = players.get(i);

            //When the current player is not eliminated, he can continue to get new card and discard
            while(currentPlayer.getInGame()){

                //The current player gets the second card and the card is removed from the deck
                currentPlayer.setCard2(deck.getDeck().get(0));
                deck.getDeck().remove(0);
                System.out.println(currentPlayer.getName() + ", it's your turn to discard.\nPlease type 1 for discarding " +
                        currentPlayer.getCard1().getCardName() + " or type 2 for discarding " + currentPlayer.getCard2().getCardName());

                //the current player choose a card in hand to discard
                Scanner sc = new Scanner(System.in);
                while(true){
                    int discard = sc.nextInt();
                    switch (discard){

                        //The current player type a number to discard the corresponding card
                        case 1:

                            //1.Special case: When the current player has countess and prince or king at the same time, he must discard the countess, or he will be asked to enter the number again
                            if(currentPlayer.getCard2().getCardName().equals("COUNTESS") && (currentPlayer.getCard1().getCardName().equals("PRINCE") || currentPlayer.getCard1().getCardName().equals("KING"))){
                                System.out.println("You must discard the countess when you have king or prince at the same time.\n Please discard the countess.");
                            }
                            //2.Special case: When the current player discards princess, he will automatically lose this round
                            else if(currentPlayer.getCard1().getCardName().equals("PRINCESS")){
                                System.out.println("You have discarded the princess, you are out of this round.");
                                currentPlayer.setInGame(false);
                                history.add(currentPlayer.getCard1());
                                history.add(currentPlayer.getCard2());
                                break;
                            }
                            //If it's not a special case, the action from the discarded card works
                            else{
                                //the corresponding action works

                            }
                        case 2:
                            if(currentPlayer.getCard1().getCardName().equals("COUNTESS") && (currentPlayer.getCard2().getCardName().equals("PRINCE") || currentPlayer.getCard2().getCardName().equals("KING"))){
                                System.out.println("You must discard the countess when you have king or prince at the same time.\n Please discard the countess.");
                            }else if(currentPlayer.getCard2().getCardName().equals("PRINCESS")){
                                System.out.println("You have discarded the princess, you are out of this round.");
                                currentPlayer.setInGame(false);
                                history.add(currentPlayer.getCard1());
                                history.add(currentPlayer.getCard2());
                                break;
                            }
                            else{
                                //the corresponding action works
                            }
                    }
                }


            }
            i++;
        }

    }
}
