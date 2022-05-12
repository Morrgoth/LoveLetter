package bb.love_letter.game;

import bb.love_letter.game.characters.Cards;

import java.util.ArrayList;
import java.util.Scanner;


/**
 * class Player extends class User. Contains all attributes and actions that a player can make during the game
 *
 * @author Veronika Heckel
 * @author Muqiu Wang
 */
public class Player extends User{

    private Cards card1;
    private Cards card2;
    private int token = 0;

    private boolean eliminated = false;
    private ArrayList<Cards> discarded = new ArrayList<>();


    public Player(String name, Cards card1, Cards card2, int token) {
        super(name);
        this.card1 = card1;
        this.card2 = card2;
        this.token = token;
    }

    public Cards getCard1(){
        return card1;
    }

    public void setCard1(Cards card1){
        this.card1 = card1;
    }

    public Cards getCard2(){
        return card2;
    }

    public void setCard2(Cards card2){
        this.card2 = card2;
    }


    public int getToken(){
        return token;
    }

    public void setToken(){
        token++;
    }

    public int discardedPoints(ArrayList<Cards> discarded){
        int sum = 0;
        for(Cards card: discarded){
            sum += card.getCardPoints();
        }
        return sum;
    }

    //player's action - draw the top most  card from deck
    public void drawCard(Deck deck){
        if(card1 == null) {
            card1 = deck.getDeck().get(0);
        }else{
            card2 = deck.getDeck().get(0);
        }
        deck.getDeck().remove(0);

    }


    //discard a Card during each round
    public void discardCard(){
        System.out.println("Choose a card: ");
        Scanner scanner = new Scanner(System.in);
        String chosenCard = scanner.nextLine();
        if(chosenCard.equals(card1.getCardName())){
            discarded.add(card1);
            setCard1(null);
        }else{
            discarded.add(card2);
                setCard2(null);
            }
        scanner.close();
        }


    //Chose a player for cardActions
    public Player choosePlayer(Player player){
        Scanner scanner = new Scanner(System.in);
        String playerName = scanner.nextLine();
        if(playerName.equals(player)){
            Player chosenPlayer = player;
        }
        return choosePlayer(player);
    }
}

