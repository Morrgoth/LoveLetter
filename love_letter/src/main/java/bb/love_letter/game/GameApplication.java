package bb.love_letter.game;

import bb.love_letter.game.characters.Cards;

import java.util.ArrayList;

public class GameApplication {
   //List of players in Game
    ArrayList<Player> players = new ArrayList<>();
    ArrayList<Cards> gameHistory = new ArrayList<>();

    User nextUser;
    Player winner = null;


    //Takes the right number of Cards from Deck at the Beginning of each round
    private void withdrawCardsAtBeginning(Deck deck){
        if(players.size() == 2){
            deck.getDeck().remove(0);
            gameHistory.add(deck.getDeck().get(0));
            for(int i = 0; i < 3; i++){
                deck.getDeck().get(0).getCardName();
                gameHistory.add(deck.getDeck().get(0));
                deck.getDeck().remove(i);
            }
        }else{
            deck.getDeck().remove(0);
            gameHistory.add(deck.getDeck().get(0));
        }
    }

    public void addPlayers(User user){
        while(players.size()<4){
            if(players.contains(user)){
                players.add(new Player(user.getName(), null, null,0));
             }
        }
    }


    //Gives every player a card at beginning of the game
    public void playerCardsBeginning(Deck deck){
        for(int i = 0; i < players.size(); i++){
            players.get(i).setCard1(deck.getDeck().get(0));
        }
    }

    public User getNextUser(User user){
        return getNextUser(user);
    }


    public boolean isGameOver(Player player){
            while(winner.equals(null)){
                return false;
            }
            return true;
        }


    public GameEvent startGame(){
        ArrayList<GameEvent> gameEvent = new ArrayList<>();
            if(players.size()>2){
                Deck deck = new Deck();
                deck.initializeDeck();
                deck.shuffleDeck();
                withdrawCardsAtBeginning(deck);
                playerCardsBeginning(deck);
                GameEvent startGame = new GameEvent();
                startGame.setType(GameEvent.GameEventType.GAMEISREADY);
                gameEvent.add(startGame);
            }

            return startGame();
        }



    public static void main(String[] args) {


    }
}
